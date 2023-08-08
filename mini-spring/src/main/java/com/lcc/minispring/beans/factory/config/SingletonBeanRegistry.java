package com.lcc.minispring.beans.factory.config;

public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);
}
