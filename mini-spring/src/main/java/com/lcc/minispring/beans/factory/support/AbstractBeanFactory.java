package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.BeanFactory;
import com.lcc.minispring.beans.factory.config.BeanDefinition;

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

    @Override
    public Object getBean(String name, Object... args) {
        Object singleton = getSingleton(name);
        if (singleton != null) {
            return singleton;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new RuntimeException("no bean named " + name + " is defined");
        }
        singleton = createBean(name, beanDefinition, args);

        return singleton;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args);

    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
