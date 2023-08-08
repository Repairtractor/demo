package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.config.BeanDefinition;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @SneakyThrows({InstantiationException.class,IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) {
        if (ctor == null) {
            return beanDefinition.clazz.newInstance();
        } else {
            return ctor.newInstance(args);
        }
    }
}
