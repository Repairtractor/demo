package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略
 */
public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args);
}
