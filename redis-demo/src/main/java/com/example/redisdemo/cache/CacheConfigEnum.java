package com.example.redisdemo.cache;


import java.time.Duration;

/**
 * @Date: 2022/2/16
 * @Author: kim
 * @Email: kim.zjy@bitsun-inc.com
 */
public enum CacheConfigEnum {




    ;

    /**
     * 缓存配置标示
     */
    public final String code;
    /**
     * 缓存key
     */
    public final String key;

    /**
     * 缓存最大容量
     */
    public final int maxCapacity;
    /**
     * 过期时间
     */
    public final long expireTime;

    public final boolean isRetrySynchronizer;



    CacheConfigEnum(String code, String key) {
        this(code, key, -1, -1L);
    }

    public String path() {
        return key + RedisCacheConstant.CacheKeyComa + code;
    }


    CacheConfigEnum(String code, String key, int maxCapacity, Duration expireTime) {
        this(code, key, maxCapacity, expireTime.toMillis());
    }

    CacheConfigEnum(String code, String key, int maxCapacity, long expireTime) {
        this(code, key, maxCapacity, expireTime, false);
    }


    CacheConfigEnum(String code, String key, int maxCapacity, long expireTime, boolean isRetrySynchronizer) {
        this.code = code;
        this.key = key;
        this.maxCapacity = maxCapacity;
        this.expireTime=expireTime;
        this.isRetrySynchronizer = isRetrySynchronizer;
    }



    /**
     * redisson 常量配置
     */
    public interface RedisCacheConstant {

        String CacheKeyComa = ":";

        //redisson 锁的基础时间
        Integer baseLockTime = 40;

        //redisson 锁的增加时间
        Integer bound = 20;

        String PREFIX = "yt:bi:forecast";

        //redisson配置文件路径
        String CACHE_PATH = PREFIX + CacheKeyComa + "cache";


        //redisson set路径

        String SET_PATH = PREFIX + CacheKeyComa + "set";

        //redisson锁路径

        String LOCK_PATH = PREFIX + CacheKeyComa + "lock";

        /**
         * redis stream name
         */

        String STREAM_NAME = PREFIX + CacheKeyComa + "stream";

        /**
         * redis stream consumer group
         */
        String REDIS_STREAM_CONSUMER_GROUP = "redisCacheRetryGroup";

        /**
         * redis stream consumer name
         */
        String CONSUMER_NAME = "redisCacheRetryConsumer";


        /**
         * 队列中的重试次数
         */
        int RETRY_TIME = 2;

        int FAIL_MAX_SIZE = 500;

    }


}
