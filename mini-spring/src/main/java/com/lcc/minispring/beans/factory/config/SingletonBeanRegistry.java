package com.lcc.minispring.beans.factory.config;

import java.util.Map;

public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);

}
