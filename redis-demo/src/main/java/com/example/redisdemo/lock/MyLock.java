package com.example.redisdemo.lock;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 测试分布式锁
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MyLock {

    final RedissonClient redisson;

    private volatile int count = 0;


    @GetMapping("/testLock")
    public void testLock() {
        assert redisson != null;
        extracted1();
        log.error("cccccccccccccccccccccccccccccccccccccccc");
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

    private void extracted1() {
        //自增count 序列
        RLock lock = redisson.getLock("lock");

        lock.lock();
        long count1 = redisson.getAtomicLong("count").incrementAndGet();
        System.out.println("当前count值为：" + count1);
        lock.unlock();
    }

    @GetMapping("/count")
    public Long count() {
        System.out.println("本服务执行了" + count + "次");
        assert redisson != null;

        boolean exists = redisson.getAtomicLong("count").isExists();
        if (!exists) {
            redisson.getAtomicLong("count").set(0);
        }
        return redisson.getAtomicLong("count").get();
    }


}
