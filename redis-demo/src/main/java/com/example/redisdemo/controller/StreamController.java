package com.example.redisdemo.controller;

import cn.hutool.core.map.MapUtil;
import com.example.redisdemo.stream.StreamPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/stream")
public class StreamController {

    final StreamPublisher streamPublisher;

    @GetMapping("/send/task")
    public String send(@RequestParam("task") String task) {
        streamPublisher.publish("门派任务", MapUtil.of("task", task));
        return "消息发送成功";
    }


}
