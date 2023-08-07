package com.lcc.minispring.ioc.support;

import com.lcc.minispring.ioc.config.BeanDefinition;

import java.util.Map;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {


    @Override
    public Object getBean(String name) {
        Object singleton = getSingleton(name);
        if (singleton != null) {
            return singleton;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new RuntimeException("no bean named " + name + " is defined");
        }
        singleton = createBean(name, beanDefinition);

        return singleton;
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args);

    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
