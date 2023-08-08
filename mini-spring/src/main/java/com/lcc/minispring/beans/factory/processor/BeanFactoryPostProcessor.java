package com.lcc.minispring.beans.factory.processor;

import com.lcc.minispring.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.BeansException;

public interface BeanFactoryPostProcessor {

    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制
     *
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
