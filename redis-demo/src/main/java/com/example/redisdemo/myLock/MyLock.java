package com.example.redisdemo.myLock;


import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 测试分布式锁
 */
@RestController
@RequiredArgsConstructor
public class MyLock {

    final RedissonClient redisson;

    private volatile int count = 0;


    @GetMapping("/testLock")
    public void testLock() {
        assert redisson != null;
        extracted();
        count++;
    }

    private synchronized void extracted() {
        Object count = redisson.getBucket("count").get();
        System.out.println("hello world");
        if (Objects.isNull(count)) {
            count = 1;
        }

        redisson.getBucket("count").set((Integer) count + 1);
    }

    @GetMapping("/count")
    public Integer count() {
        System.out.println("本服务执行了" + count + "次");
        assert redisson != null;
        Object count = redisson.getBucket("count").get();
        if (Objects.isNull(count)) {
            redisson.getBucket("count").set(1);
        }
        return (Integer) count;
    }


}
