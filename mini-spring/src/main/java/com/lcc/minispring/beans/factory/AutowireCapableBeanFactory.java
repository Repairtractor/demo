package com.lcc.minispring.beans.factory;

import org.springframework.beans.BeansException;

public interface AutowireCapableBeanFactory extends BeanFactory{
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
