package com.lcc.spi.spidemo.spis.impl;

import com.lcc.spi.spidemo.spis.Animal;

public class Dog implements Animal {
    @Override
    public void eat() {
        System.out.println("dog eat");
    }
}
