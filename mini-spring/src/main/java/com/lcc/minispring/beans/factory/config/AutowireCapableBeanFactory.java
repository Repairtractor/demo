package com.lcc.minispring.beans.factory.config;

import com.lcc.minispring.beans.factory.BeanFactory;
import org.springframework.beans.BeansException;

public interface AutowireCapableBeanFactory extends BeanFactory {
     Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
     Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
