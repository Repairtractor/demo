package com.example.redisdemo;

import com.example.redisdemo.pub.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/pub")
@RequiredArgsConstructor
public class PubStreamController {

    final Publisher publisher;

    @GetMapping("/test")
    public String test(){
        publisher.publish("test","hello world");
        return "消息发送成功";
    }
}
