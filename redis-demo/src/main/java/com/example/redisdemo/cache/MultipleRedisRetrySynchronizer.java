package com.example.redisdemo.cache;

import cn.hutool.core.collection.CollUtil;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 同步器，目前是利用redis的发布订阅做异步通知，使用redisson的分布式锁做同步
 *
 * @param <V>
 */
public class MultipleRedisRetrySynchronizer<V> extends AbstratctRedisRetrySynchronizer<V> {

    private final RedisLockComponent redisLockComponent;

    private final RStream<String, ReTime> rStream;



    public MultipleRedisRetrySynchronizer(String keyPath, RedissonClient redisson, CacheConfigEnum config, RedisLockComponent redisLockComponent) {
        super(keyPath, redisson, config);
        this.redisLockComponent = redisLockComponent;
        //初始化redis stream
        this.rStream = redisson.getStream(CacheConfigEnum.RedisCacheConstant.STREAM_NAME);
        //初始化消费组
        rStream.createGroup(CacheConfigEnum.RedisCacheConstant.REDIS_STREAM_CONSUMER_GROUP, StreamMessageId.ALL);
        init();
    }


    protected Collection<ReTime> consume() {
        Map<StreamMessageId, Map<String, ReTime>> streamMessageIdMapMap = rStream.readGroup(CacheConfigEnum.RedisCacheConstant.REDIS_STREAM_CONSUMER_GROUP, CacheConfigEnum.RedisCacheConstant.CONSUMER_NAME);
        if (CollUtil.isEmpty(streamMessageIdMapMap)) {
            return Collections.emptyList();
        }
        rStream.ack(CacheConfigEnum.RedisCacheConstant.REDIS_STREAM_CONSUMER_GROUP, streamMessageIdMapMap.keySet().toArray(new StreamMessageId[0]));
        return streamMessageIdMapMap.values().stream().flatMap(it -> it.values().stream()).collect(Collectors.toList());
    }

    @Override
    public boolean lock(Collection<String> keys) {
        Set<String> lockKeys = keys.parallelStream().map(it -> CacheConfigEnum.RedisCacheConstant.LOCK_PATH + it).collect(Collectors.toSet());
        redisLockComponent.multiLock(lockKeys, CacheConfigEnum.RedisCacheConstant.baseLockTime);
        return true;
    }

    @Override
    public void unLock(Collection<String> keys) {
        redisLockComponent.unlock();
    }


    @Override
    protected void retry(ReTime reTime) {
        RStream<String, ReTime> rStream = redisson.getStream(CacheConfigEnum.RedisCacheConstant.STREAM_NAME);
        rStream.add("retry", reTime);
    }

    @Override
    protected int retryingSize() {
        return (int) rStream.size();
    }

}
