package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private final Map<Class<?>,Object> singletonFactories = new ConcurrentHashMap<>(16);

    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        singletonFactories.put(singletonObject.getClass(),singletonObject);
    }


    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }
}
