package com.example.redisdemo;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBatch;
import org.redisson.api.RBuckets;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootTest
public class ScanTest {

    @Resource
    RedissonClient redisson;

    /**
     * 生成一个测试方法，向redis中插入10000个key，key的前缀为user_token_，后面跟上随机的0-10000的数字
     */
    @Test
    public void testInsert() {
        RBuckets buckets = redisson.getBuckets(StringCodec.INSTANCE);
        MapBuilder<String, Object> builder = MapUtil.builder();

        IntStream.rangeClosed(1, 10_000)
                 .forEach(it-> builder.put("user_token_" + it, it));

        buckets.set(builder.build());
    }


    @Test
    public void testScan() {
        RKeys keys = redisson.getKeys();
//        Iterable<String> keysByPattern = keys.getKeysByPattern("user_token_*");
//        List<String> arr=new ArrayList<>();
//        keysByPattern.forEach(arr::add);
        keys.deleteByPattern("user_token_*");


    }
}
