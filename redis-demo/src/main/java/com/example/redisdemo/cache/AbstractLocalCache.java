package com.example.redisdemo.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.redisdemo.cache.LogEnum.CACHE_INFO;


public abstract class AbstractLocalCache<K, V, T> implements Cache<K, V, T> {

    private final GetSetter<T, K, V> getSetter;

    private LoadingCache<K, V> cache;

    private final CacheConstant cacheConstant;


    private static final CacheConstant CACHE_DEFAULT = new CacheConstant(1000, 1000 * 60);


    protected CacheLoader<K, V> getCacheCallBack() {
        return new CacheLoader<K, V>() {
            @Override
            public V load(K key) throws Exception {
                Map<K, V> kvMap = getCacheKeyAndValue(Collections.singletonList(key));
                if (MapUtil.isEmpty(kvMap)) {
                    return null;
                }
                return kvMap.get(key);
            }

            @Override
            public @NonNull Map<@NonNull K, @NonNull V> loadAll(@NonNull Iterable<? extends @NonNull K> keys) throws Exception {
                List<K> list = new ArrayList<>();
                keys.forEach(list::add);
                CACHE_INFO.info("本地缓存没有命中，从数据库获取数据，keys：{}", CollUtil.join(keys, ","));
                return getCacheKeyAndValue(list);
            }

        };
    }


    protected AbstractLocalCache(CacheConfigEnum configEnum, GetSetter<T, K, V> getSetter) {
        this(getSetter, configEnum.maxCapacity >= 0
                && configEnum.expireTime >= 0
                ? new CacheConstant(configEnum.maxCapacity, configEnum.expireTime)
                : CACHE_DEFAULT);
    }

    protected AbstractLocalCache(GetSetter<T, K, V> getSetter) {
        this(getSetter, CACHE_DEFAULT);
    }

    protected AbstractLocalCache(GetSetter<T, K, V> getSetter, CacheConstant cacheConstant) {
        this.getSetter = getSetter;
        this.cacheConstant = cacheConstant == null ? CACHE_DEFAULT : cacheConstant;
        initCache();
    }


    private void initCache() {
        CACHE_INFO.info("初始化本地缓存，缓存大小：{}，缓存超时时间：{}ms", cacheConstant.maxSize, cacheConstant.expireTime);
        this.cache = Caffeine.newBuilder()
                .maximumSize(cacheConstant.maxSize)
                .expireAfterWrite(cacheConstant.expireTime, TimeUnit.MILLISECONDS)
                .build(getCacheCallBack());
    }

    @Override
    public void removeKey(Collection<K> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void put(Collection<T> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Map<K, V> kvMap = list.stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
        cache.putAll(kvMap);
    }

    @Override
    public Map<K, V> asMap(Collection<K> keys) {
        try {
            return cache.getAll(keys);
        } catch (Exception e) {
            throw new RuntimeException("从缓存获取数据失败了");
        }
    }

    protected Map<K, V> getCacheKeyAndValue(List<K> keys) {
        return sourceAll(keys).stream().collect(Collectors.toMap(getSetter.keyGetter, getSetter.valueGetter));
    }


    @AllArgsConstructor
    protected static class CacheConstant {
        public final int maxSize;
        public final long expireTime;
    }


}
