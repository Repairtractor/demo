package com.example.redisdemo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.FstCodec;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class RedisDemoApplicationTests {

    RedissonClient redissonClient;

    @BeforeEach
    public void init() {
        redissonClient = SpringUtil.getBean(RedissonClient.class);
    }


    @Test
    void contextLoads() throws InterruptedException {

        //多线程测试以下代码
        int max = 5;

        CountDownLatch countDownLatch = new CountDownLatch(max);

        for (int i = 0; i < max; i++) {

            new Thread(() -> {
                testRedis3(null);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();

    }

    /**
     * 使用setnx实现分布式锁
     * 1.加过期时间，防止死锁
     * 2.使用标识，防止误删其他线程的锁
     * 3.使用lua脚本，保证判断标识和删除操作是原子操作
     * 4.优化为使用map存储，key为标识，value为重入次数，优化为可重入锁
     */
    private void testRedis3(String key) {
        //使用uuid作为标识，防止误删其他线程的锁
        String uuid = key == null ? UUID.fastUUID().toString() : key;

        if (Boolean.TRUE.equals(tryLock(uuid))) {
            log.info("线程{}获取锁成功", Thread.currentThread().getName());
            ThreadUtil.sleep(2000);

            if (Boolean.TRUE.equals(tryLock(uuid))) {
                log.info("线程{}重入锁成功", Thread.currentThread().getName());
                if (Boolean.TRUE.equals(unLock(uuid))) {
                    log.info("线程{}释放锁成功", Thread.currentThread().getName());
                } else {
                    log.info("线程{}释放锁失败", Thread.currentThread().getName());
                }

            } else {
                log.info("线程{}重入锁失败", Thread.currentThread().getName());
            }

            if (Boolean.TRUE.equals(unLock(uuid))) {
                log.info("线程{}释放锁成功", Thread.currentThread().getName());
            } else {
                log.info("线程{}释放锁失败", Thread.currentThread().getName());
            }

        } else {
            log.info("线程{}获取锁失败,睡一会", Thread.currentThread().getName());
            ThreadUtil.sleep(1000);
            testRedis3(uuid);
        }
    }

    /**
     * 异步线程，执行续期
     *
     * @param uuid
     * @return
     */
    private void RenewalExpire(String uuid, String key) {
        String script = "if(redis.call('hexists', KEYS[1], ARGV[1]) == 1) then redis.call('expire', KEYS[1], ARGV[2]); return 1; else return 0; end";
        new Thread(() -> {

            //异步线程，如果是当前线程持有锁，直接续期一倍
            do {
                //先睡过期时间的3分之二时间
                ThreadUtil.sleep(1000 / 3 * 2);
                //然后进行续期
            } while (Boolean.TRUE.equals(redissonClient.getScript().eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.BOOLEAN, Arrays.asList(key), uuid)));

        }).start();
    }


    private boolean tryLock(String uuid) {
        RScript script = redissonClient.getScript();
        String lua = IoUtil.read(FileUtil.getReader("lua/HashLock.lua", "utf-8"));
        Object success = script.eval(RScript.Mode.READ_WRITE, lua, RScript.ReturnType.BOOLEAN, Arrays.asList("lock"), uuid);

        RenewalExpire(uuid, "lock");
        return Boolean.TRUE.equals(success);
    }

    private boolean unLock(String uuid) {
        RScript script = redissonClient.getScript();
        String lua1 = IoUtil.read(FileUtil.getReader("lua/HashUnLock.lua", "utf-8"));
        Object unLock = script.eval(RScript.Mode.READ_WRITE, lua1, RScript.ReturnType.BOOLEAN, Arrays.asList("lock"), uuid.toString());
        return Boolean.TRUE.equals(unLock);
    }


    /**
     * 使用setnx实现分布式锁
     * 1.加过期时间，防止死锁
     * 2.使用标识，防止误删其他线程的锁
     * 3.使用lua脚本，保证判断标识和删除操作是原子操作
     * 这种方式虽然解决了误删问题，但是判断标识和删除操作还不是原子操作
     */
    private void testRedis2() {
        //使用uuid作为标识，防止误删其他线程的锁
        UUID uuid = UUID.fastUUID();
        RBucket<Object> lock = redissonClient.getBucket("lock");
        boolean success = lock.setIfAbsent(uuid, Duration.ofSeconds(1));

        if (success) {
            log.info("线程{}获取锁成功", Thread.currentThread().getName());
            //释放锁
            RScript script = redissonClient.getScript();
            String lua = IoUtil.read(FileUtil.getReader("lua/unlock.lua", "utf-8"));
            Object eval = script.eval(RScript.Mode.READ_WRITE, lua, RScript.ReturnType.BOOLEAN, Arrays.asList("lock"), uuid.toString());
            if (Boolean.TRUE.equals(eval)) {
                log.info("线程{}释放锁成功", Thread.currentThread().getName());
            } else {
                log.info("线程{}释放锁失败", Thread.currentThread().getName());
            }

        } else {
            log.info("线程{}获取锁失败,睡一会", Thread.currentThread().getName());
            ThreadUtil.sleep(1000);
            testRedis();
            log.info("线程{}睡醒了", Thread.currentThread().getName());
        }
    }


    /**
     * 使用setnx实现分布式锁
     * 加过期时间，防止死锁
     * 使用标识，防止误删其他线程的锁
     * 这种方式虽然解决了误删问题，但是判断标识和删除操作还不是原子操作
     */
    private void testRedis1() {
        //使用uuid作为标识，防止误删其他线程的锁
        UUID uuid = UUID.fastUUID();
        RBucket<Object> lock = redissonClient.getBucket("lock");
        boolean success = lock.setIfAbsent(uuid, Duration.ofSeconds(1));

        if (success) {
            log.info("线程{}获取锁成功", Thread.currentThread().getName());
            //释放锁
            if (uuid.equals(lock.get())) {
                boolean lock1 = lock.delete();
                if (lock1)
                    log.info("线程{}释放锁成功", Thread.currentThread().getName());
            }
        } else {
            log.info("线程{}获取锁失败,睡一会", Thread.currentThread().getName());
            ThreadUtil.sleep(1000);
            testRedis();
            log.info("线程{}睡醒了", Thread.currentThread().getName());
        }
    }


    /**
     * 测试redis锁，只是简单的使用setnx加过期时间实现
     * 这种方式有一个问题，如果获取锁的线程执行时间超长，导致锁过期，其他线程获取到锁，这时候原来的线程执行完了，会删除掉其他线程的锁
     */
    private void testRedis() {
        boolean lock = redissonClient.getBucket("lock").setIfAbsent("1", Duration.ofSeconds(1));
        if (lock) {
            log.info("线程{}获取锁成功", Thread.currentThread().getName());
            //do sh
            ThreadUtil.sleep(2000);
            boolean lock1 = redissonClient.getBucket("lock").delete();
            if (lock1)
                log.info("线程{}释放锁成功", Thread.currentThread().getName());
            else
                log.error("线程{}释放锁失败", Thread.currentThread().getName());
        } else {
            log.info("线程{}获取锁失败,睡一会", Thread.currentThread().getName());
            ThreadUtil.sleep(1000);
            testRedis();
            log.info("线程{}睡醒了", Thread.currentThread().getName());
        }
    }


    @Test
    public void testSerialization() {
        RBuckets buckets = redissonClient.getBuckets(JsonJacksonCodec.INSTANCE);
        redissonClient.getBucket("cc").get();
        buckets.set(MapUtil.of("test", "test"));
    }

    @Data
    @Accessors(chain = true)
    public static class Person {
        public String name;
        public Integer age;
    }

}
