package com.lcc.minispring.beans.factory.config;


import com.lcc.minispring.beans.factory.HierarchicalBeanFactory;


public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";


    String SCOPE_PROTOTYPE = "prototype";


    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

}
