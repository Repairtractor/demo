package com.lcc.minispring.ioc.support;

import com.lcc.minispring.ioc.config.BeanDefinition;
import lombok.SneakyThrows;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @SneakyThrows
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        //这里通过bean定义new bean 然后放入单例池
        registerSingleton(beanName, beanDefinition.clazz.newInstance());
        return getBean(beanName);
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        if (args == null || args.length == 0) {
            return createBean(beanName, beanDefinition);
        }
        return null;

    }
}
