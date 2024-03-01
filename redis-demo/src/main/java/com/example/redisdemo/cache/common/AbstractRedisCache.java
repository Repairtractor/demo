package com.example.redisdemo.cache.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.example.redisdemo.cache.common.synchronizer.ConcurrencyUtil;

import com.example.redisdemo.cache.common.synchronizer.RedisRetrySynchronizer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBuckets;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * @param <V> 缓存值类型
 * @param <T> 缓存实体
 * @Description: redis缓存抽象类
 */
@Slf4j
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

    RScript rScript;


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
        this.rScript=redisson.getScript(StringCodec.INSTANCE);
        log.info("RedisCache initialized with key path: {},cacheCode:{},", keyPath, config.getCode());
    }


    protected AbstractRedisCache(CacheConfig config, GetSetter<T, String, V> getSetter) {
        this(config, getSetter, false);
    }


    @Override
    public void removeKey(Collection<String> codes) {
        log.debug("Attempting to delete keys from Redis cache: {}", codes);
        List<String> keys = codes.stream().map(it -> keyPath + it).collect(toList());
        try {
            redisson.getKeys().delete(keys.toArray(new String[0]));
        } catch (Exception exception) {
            log.error("Failed to delete Redis cache, transferring to queue for retry, keys: {}", codes);
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
        Map<String, V> map = list.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
        put(map);
    }

    @Override
    public void put(Map<String, V> map) {
        try {
            log.debug("Putting map into Redis cache: {}", JSONUtil.toJsonStr(map));
            Map<String, String> putMap = convertToCacheMap(map);
            rBuckets.set(putMap);
            syncExpire(putMap);
        } catch (Exception exception) {
            log.error("Batch data insertion failed, transferring to queue for retry");
            map.forEach(this::retry);
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
        ConcurrencyUtil.execute(() -> {
            log.info("Synchronizing expiration time for keys: {}", redisKeyValueMap.keySet());
            redisKeyValueMap.keySet().forEach(it -> redisson.getKeys().expire(it, config.getExpireTime(), TimeUnit.MILLISECONDS));
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
            return cacheMap2SourceMap(map);
        }
        retrySet(reKeys);
        return get(keys, false);
    }

    @NotNull
    private Map<String, V> cacheMap2SourceMap(Map<String, String> cacheMap) {
        Map<String, V> res = new HashMap<>(cacheMap.size());
        cacheMap.forEach((k, v) -> {
            Pair<String, V> pair = convertKeyAndValue(k, v);
            res.put(pair.getKey(), pair.getValue());
        });
        return res;
    }

    private Pair<String, V> convertKeyAndValue(String k, String value) {
        return Pair.of(k.replace(keyPath, ""), JSONUtil.isJson(value) ? JSONUtil.toBean(value, new TypeReference<V>() {
        }, false) : (V) value);
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
            log.info("Retrying to set and get values for keys: {}", codes);
            if (!isRetrySynchronizer || redisRetrySynchronizer.lock(codes)) {
                Map<String, V> map = sourceMap(codes);
                put(map);
                return map;
            }
            return get(codes, false);
        } catch (Exception ex) {
            log.error("Cache build failed, keys for this build: {},message:{}", codes,ex.getMessage());
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


    /**
     * 从源数据获取数据
     *
     * @param keys
     * @return
     */
    private Map<String, V> sourceMap(Collection<String> keys) {
        log.info("Getting source map for keys: {}", keys);
        List<T> dates = sourceAll(keys).stream()
                .filter(it -> getSetter.keyGetter.apply(it) != null)
                .filter(it -> getSetter.valueGetter.apply(it) != null)
                .collect(toList());
        return dates.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
    }

    private Map<String, String> convertToCacheMap(Map<String, V> sourceMap) {
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        sourceMap.forEach((k, v) -> res.put(keyPath + k, JSONUtil.toJsonStr(v)));
        return res;
    }


    @Override
    public Collection<V> removeAndGet(Collection<String> codes) {
        log.debug("Removing and getting values for keys: {}", codes);
        Map<String, V> sourceMap = sourceMap(codes);
        Map<String, String> res = convertToCacheMap(sourceMap);
        // 准备键和值的列表
        List<String> keys = new ArrayList<>(res.keySet());
        List<Object> values = new ArrayList<>(res.values());
        //先前置删除缓存
        removeKey(codes);
        // Lua 脚本
        String script = "for i=1,#KEYS do redis.call('del', KEYS[i]); redis.call('set', KEYS[i], ARGV[i]); end";

        // 执行 Lua 脚本
        rScript.eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.VALUE, Arrays.asList(keys.toArray()), values.toArray());
        //异步设置超时时间
        syncExpire(res);
        return sourceMap.values();
    }

}
