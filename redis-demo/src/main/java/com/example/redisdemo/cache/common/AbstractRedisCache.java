package com.example.redisdemo.cache.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.redisson.api.RBuckets;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @param <V> 缓存值类型
 * @param <T> 缓存实体
 * @Description: redis缓存抽象类
 */
public abstract class AbstractRedisCache<V, T> implements Cache<String, V, T> {

    //缓存key前缀
    protected String keyPath;

    //keyValue获取器
    private final GetSetter<T, String, V> getSetter;

    //是否开启重试同步器
    private final boolean isRetrySynchronizer;

    //重试同步器
    private RedisRetrySynchronizer<V> redisRetrySynchronizer;

    //缓存配置
    private final CacheConfig config;


    @Resource
    private RedissonClient redisson;

    RBuckets rBuckets;



    /**
     * @param getSetter           keyValue获取器
     * @param isRetrySynchronizer 缓存同步器，默认false，不开启时不会进入同步机制，一致性较差，性能较好，存在缓存穿透问题，建议开启
     * @param config              缓存配置
     */
    protected AbstractRedisCache(CacheConfig config, GetSetter<T, String, V> getSetter, boolean isRetrySynchronizer) {
        this.getSetter = getSetter;
        this.isRetrySynchronizer = isRetrySynchronizer;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        this.keyPath = config.getCachePath() + RedisCacheConstant.CacheKeyComa;
        this.rBuckets = redisson.getBuckets(StringCodec.INSTANCE);
    }


    protected AbstractRedisCache(CacheConfig config, GetSetter<T, String, V> getSetter) {
        this(config, getSetter, false);
    }


    @Override
    public void removeKey(Collection<String> codes) {
        LogUtils.info("开始删除redis缓存,keys：{}", codes);
        List<String> keys = codes.stream().map(it -> keyPath + it).collect(toList());
        try {
            redisson.getKeys().delete(keys.toArray(new String[0]));
        } catch (Exception exception) {
            LogUtils.info("删除redis缓存失败了，移送队列尝试重试，keys:{}", codes);

            codes.forEach(it -> retry(it, null));
        }
    }

    private void retry(String k, V v) {
        if (isRetrySynchronizer)
            redisRetrySynchronizer.retry(k, v);
    }


    @Override
    public void put(Collection<T> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        try {
            Map<String, String> redisKeyValueMap = list.stream().collect(Collectors.toMap(getSetter.keyGetter.andThen(it -> keyPath + it), getSetter.valueGetter.andThen(JSONUtil::toJsonStr)));
            rBuckets.set(redisKeyValueMap);
            syncExpire(redisKeyValueMap);
        } catch (Exception exception) {
            LogUtils.error("批量添加数据失败，移送队列进行重试");
            list.forEach(it -> retry(getSetter.keyGetter.apply(it), getSetter.valueGetter.apply(it)));
        }
    }

    /**
     * 设置超时时间
     *
     * @param redisKeyValueMap
     */
    private void syncExpire(Map<String, String> redisKeyValueMap) {
        //如果没有设置超时时间，直接返回
        if (config.getExpireTime() <= 0) {
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            redisKeyValueMap.keySet().forEach(it -> redisson.getKeys().expire(it, config.getExpireTime(), TimeUnit.SECONDS));
        });
    }


    /**
     * @param keys           redis的key集合
     * @param weatherToReSet 有没有命中的key，是否尝试从原数据添加
     */
    private Map<String, V> get(Collection<String> keys, boolean weatherToReSet) {
        //如果开启了重试同步器，且当前正在进行同步，直接走源数据
        if (isRetrySynchronizer && !redisRetrySynchronizer.isReady(keys)) {
            return retrySetAndGetValues(keys);
        }

        Map<String, String> map = rBuckets.get(keys.stream().map(it -> keyPath + it).toArray(String[]::new));

        //获取没有命中的key
        List<String> reKeys = keys.stream().filter(it -> !map.containsKey(keyPath + it)).collect(toList());

        //如果没有命中的key为空，直接返回，或者不需要从原数据添加
        if (CollUtil.isEmpty(reKeys) || !weatherToReSet) {
            Map<String, V> result = new HashMap<>();
            map.forEach((k, v) -> {
                result.put(k.replace(keyPath, ""), JSONUtil.isJson(v) ?
                        JSONUtil.toBean(v, new TypeReference<V>() {
                        }, false) : (V) v);
            });
            LogUtils.info("查询缓存结束。本次缓存查询结果数量:{},查询keys:{}", result.size(), CollUtil.sub(keys, 0, 10));
            return result;
        }
        LogUtils.info("没有全部命中缓存，从原数据添加缓存,本次缓存查询keys数量：{}，未命中缓存keys数量：{}", keys.size(), reKeys.size());
        retrySet(reKeys);
        return get(keys, false);
    }

    /**
     * 重新设置value值
     *
     * @param codes
     * @return 返回value
     */
    private void retrySet(List<String> codes) {
        retrySetAndGetValues(codes);
    }


    //这里是否有多线程问题呢？有，会重复建立缓存，但是问题在于锁什么？怎么判定重复？
    private Map<String, V> retrySetAndGetValues(Collection<String> codes) {

        try {
            //获取锁成功，进行缓存构建,失败挂起当前线程等待锁线程释放锁
            if (!isRetrySynchronizer || redisRetrySynchronizer.lock(codes)) {
                List<T> dates = sourceAll(codes);
                put(dates);
                return dates.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
            }
            return get(codes, false);
        } catch (Exception ex) {
            LogUtils.error("构建缓存失败，本次构建keys：{}", CollUtil.join(codes, StrUtil.COMMA));
            throw ex;
        } finally {
            if (isRetrySynchronizer)
                redisRetrySynchronizer.unLock(codes);
        }
    }

    @Override
    public Map<String, V> asMap(Collection<String> keys) {
        return get(keys, true);
    }

}
