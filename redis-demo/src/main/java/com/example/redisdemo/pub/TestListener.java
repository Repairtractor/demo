package com.example.redisdemo.pub;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * redis的发布订阅模式
 */
@RequiredArgsConstructor
@Component
public class TestListener implements ApplicationRunner {
    final RedissonClient redissonClient;

    final String topic = "test";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        listen(topic);
    }

    public void listen(String topic) {
        redissonClient.getTopic(topic)
                .addListener(String.class, (channel, msg) -> {
                    System.out.println("收到来自"+channel+"的消息：" + msg);
                });
    }
}
