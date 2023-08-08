package com.lcc.minispring.beans.factory;

/**
 * 扩展beanFactory，提供了获取beanDefinition的方法
 */
public interface ListableBeanFactory extends BeanFactory {

    <T> T getBeansOfType(Class<T> type);

    String[] getBeanDefinitionNames();
}
