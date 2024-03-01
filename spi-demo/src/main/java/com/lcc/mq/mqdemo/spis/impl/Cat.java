package com.lcc.mq.mqdemo.spis.impl;

import com.lcc.mq.mqdemo.spis.Animal;

public class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("cat eat");
    }
}
