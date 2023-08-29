package com.lcc.Lock.Lockdemo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;

class LockDemoApplicationTest {

    @Test
    public void helloWorld(){
        System.out.println("hello world");
    }

    @Test
    public void testExecutors(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(()->{
            System.out.println("hello world");
        });
        executorService.shutdown();

    }

}