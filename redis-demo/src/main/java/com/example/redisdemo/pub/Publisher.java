package com.example.redisdemo.pub;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * redis的发布订阅模式
 */
@Component
@RequiredArgsConstructor
public class Publisher {

    final RedissonClient redisson;

    public void publish(String topic, String message) {
        //发布消息
        long count = redisson.getTopic(topic).publish(message);
        System.out.println("向topic为" + topic + "的频道发布了一条消息，消息内容为：" + message + "，该消息被" + count + "个订阅者接收到");
    }
}
