package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.config.ConfigurableBeanFactory;
import com.lcc.minispring.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DefaultSingletonBeanRegistry implements SingletonBeanRegistry, ConfigurableBeanFactory {

    public static final Object NULL_OBJECT = new Object();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    private final Map<Class<?>, Object> singletonFactories = new ConcurrentHashMap<>(16);

    private final Map<String, DisposableBeanAdapter> disposableBeanMap = new ConcurrentHashMap<>(16);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        singletonFactories.put(singletonObject.getClass(), singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBeanAdapter disposableBean) {
        disposableBeanMap.put(beanName, disposableBean);
    }


    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void destroySingletons() {
        for (String beanName : disposableBeanMap.keySet()) {
            DisposableBeanAdapter disposableBean = disposableBeanMap.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new RuntimeException("destroy bean error", e);
            }
        }
    }
}
