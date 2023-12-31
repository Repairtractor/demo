package com.lcc.minispring.beans.factory;

import com.lcc.minispring.beans.factory.config.BeanDefinition;

import java.util.Map;

/**
 * Bean工厂
 */
public interface BeanFactory {

    Object getBean(String name);

    Object getBean(String name, Object... args);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    <T> T getBean(String name ,Class<T> requiredType);


    <T> Map<String, T> getBeansOfType(Class<T> type);
}
