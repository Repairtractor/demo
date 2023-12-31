package com.lcc.minispring.beans.factory;

import com.lcc.minispring.beans.factory.config.BeanDefinition;

import java.util.Map;

/**
 * 扩展beanFactory，提供了获取beanDefinition的方法
 */
public interface ListableBeanFactory extends BeanFactory {

    <T> Map<String,T> getBeansOfType(Class<T> type);

    String[] getBeanDefinitionNames();

    BeanDefinition getBeanDefinition(String beanName);


}
