package com.example.redisdemo.cache.common;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 本地缓存的抽象类
 *
 * @param <V>
 * @param <T>
 */
public class DefaultLocalCache<V, T> extends AbstractLocalCache<String, V, T> {

    private final Function<Collection<String>, List<T>> allSource;


    public static <V, T> Builder<V, T> builder(GetSetter<T, String, V> getSetter) {
        return new Builder<>(getSetter);
    }

    public static class Builder<V, T> {
        private final GetSetter<T, String, V> getSetter;
        private Function<Collection<String>, List<T>> allSource;

        private int maxSize = 1000;

        private CacheConstant cacheConstant;



        public Builder(GetSetter<T, String, V> getSetter) {
            this.getSetter = getSetter;
        }

        public DefaultLocalCache<V, T> build() {
            return new DefaultLocalCache<>(getSetter, allSource, cacheConstant);
        }


        public Builder<V, T> allSource(Function<Collection<String>, List<T>> allSource) {
            this.allSource = allSource;
            return this;
        }

        public Builder<V, T> simpleSource(Function<String, T> simpleSource) {
            if (allSource == null) {
                allSource = (keys) -> keys.stream().map(simpleSource).collect(toList());
            }
            return this;
        }

        public Builder<V, T> expireTime(Duration expireTime) {
            this.cacheConstant = new CacheConstant(maxSize, expireTime.toMillis());
            return this;
        }

        public Builder<V, T> maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder<V, T> cacheConstant(CacheConstant cacheConstant) {
            this.cacheConstant = cacheConstant;
            return this;
        }
    }


    private DefaultLocalCache(GetSetter<T, String, V> getSetter, Function<Collection<String>, List<T>> allSource, CacheConstant cacheConstant) {
        super(getSetter, cacheConstant);
        this.allSource = allSource;
    }


    @Override
    public List<T> sourceAll(Collection<String> keys) {
        return allSource.apply(keys);
    }
}
