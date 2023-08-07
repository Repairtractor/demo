package com.lcc.minispring.ioc.support;

import com.lcc.minispring.ioc.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }


    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }
}
