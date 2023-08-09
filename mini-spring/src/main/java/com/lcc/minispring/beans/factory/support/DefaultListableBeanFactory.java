package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.lcc.minispring.beans.factory.config.BeanDefinition;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }


    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        //todo 这里不对，这里应该是可以初始化bean的
        String[] beanDefinitionNames = getBeanDefinitionNames();
        Map<String, T> result = new ConcurrentHashMap<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = getBeanDefinition(beanDefinitionName);
            if (type.isAssignableFrom(beanDefinition.clazz)) {
                result.put(beanDefinitionName, (T) getBean(beanDefinitionName));
            }
        }
        return result;
    }
}
