package com.lcc.minispring.ioc.config;

public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);
}
