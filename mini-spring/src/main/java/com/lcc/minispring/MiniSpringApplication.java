package com.lcc.minispring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniSpringApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(MiniSpringApplication.class, args);
    }

}
