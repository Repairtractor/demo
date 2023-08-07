package com.lcc.minispring.ioc.config;

/**
 * Bean定义
 */
public class BeanDefinition {
    public final Class<?> clazz;

    public BeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }
}
