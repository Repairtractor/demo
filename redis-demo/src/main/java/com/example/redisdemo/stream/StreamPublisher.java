package com.example.redisdemo.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * redis 的stream 发布消息流
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StreamPublisher {

    final RedissonClient redisson;



    public static String topic = "门派任务";

    /**
     * 发布redis 消息
     *
     * @param topic   标题，redis中的stream流
     * @param message 消息
     */
    public void publish(String topic, Map<Object,Object>message) {
        RStream<Object, Object> stream = redisson.getStream(topic,new StringCodec());

        StreamMessageId id = stream.add(StreamAddArgs.entries(message));
        log.info("发送消息成功！消息id：{}",id);
    }
}























