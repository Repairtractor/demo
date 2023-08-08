package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    boolean containsBeanDefinition(String beanName);
}
