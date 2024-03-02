package com.example.redisdemo.stream;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.redisson.api.StreamMessageId.NEVER_DELIVERED;

@Component
@Slf4j
@RequiredArgsConstructor
public class Consumer1 implements ApplicationRunner {


    @Value("${streamName:}")
    String streamName;

    @Value("${groupName:}")
    String groupName;


    final RedissonClient redisson;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (StrUtil.isBlank(streamName) || StrUtil.isBlank(groupName)) {
            log.info("当前服务没有配置消费组或者消费者，不会进行消费");
            return;
        }
        //异步线程启动进行消费
        CompletableFuture.runAsync(() -> {
            consumer(StreamPublisher.topic, groupName, streamName);
        }, Executors.newSingleThreadExecutor());


    }

    /**
     * 消费者
     *
     * @param topic        消费者标题，相当于redis 的stream名称
     * @param group        消费者组，一般一个服务就一个，一个消息只会被一个消费者消费
     * @param consumerName 消费组名称
     */
    public void consumer(String topic, String group, String consumerName) {
        RStream<Object, Object> stream = redisson.getStream(topic, new StringCodec());
        while (true){
            log.info("初始化消费者成功！消费组：{} 消费者：{} 监听topic：{}", groupName, streamName, StreamPublisher.topic);
            Map<StreamMessageId, Map<Object, Object>> messageIdMapMap = stream.readGroup(group, consumerName, StreamReadGroupArgs.
                    greaterThan(StreamMessageId.NEVER_DELIVERED)
                    .count(100)
                    .timeout(Duration.ofSeconds(100))
            );

            log.info("消息消费成功！进行消息确认，消息id：{}", messageIdMapMap.keySet());
            stream.ack(group, messageIdMapMap.keySet().toArray(new StreamMessageId[0]));
        }

    }
}
