package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.BeanFactory;
import com.lcc.minispring.beans.factory.ConfigurableBeanFactory;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.processor.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {


    private final List<BeanPostProcessor> beanPostProcessors;


    public AbstractBeanFactory() {
        this.beanPostProcessors = new ArrayList<>();
    }

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
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) getBean(name);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return null;
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args);

    protected abstract BeanDefinition getBeanDefinition(String beanName);


}
