package com.lcc.minispring.beans.factory;


import com.lcc.minispring.beans.factory.processor.BeanPostProcessor;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


}
