package com.lcc.minispring.DTO;

import com.lcc.minispring.beans.PropertyValue;
import com.lcc.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.BeansException;

public class MyBeanFactoryProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("在这里对BeanFactory进行修改");
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("beanDefinitionName = " + beanDefinitionName);
            if (!beanDefinitionName.equals("userDaoService")) {
                continue;
            }
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue("company", "alibaba"));
        }
    }
}
