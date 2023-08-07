package com.lcc.minispring.ioc.support;

import com.lcc.minispring.ioc.config.BeanDefinition;

public interface BeanDefinitionRegistry {
     void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
