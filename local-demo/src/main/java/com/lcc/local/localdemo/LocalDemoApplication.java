package com.lcc.local.localdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocalDemoApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(LocalDemoApplication.class, args);
    }

}
