package com.lcc.spi.spidemo;

import com.lcc.spi.spidemo.spis.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ServiceLoader;

@SpringBootTest
class SpiDemoApplicationTests {

    /**
     * 测试 jdk spi , spring jdk dubbo 并没有明显的区别
     */
    @Test
    void jdkSpiTest() {
        ServiceLoader<Animal> load = ServiceLoader.load(Animal.class);
        for (Animal animal : load) {
            animal.eat();
        }
    }

    @Test
    void springSpiTest() {
        // TODO
    }



}
