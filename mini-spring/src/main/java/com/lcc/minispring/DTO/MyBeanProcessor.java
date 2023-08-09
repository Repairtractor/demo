package com.lcc.minispring.DTO;

import com.lcc.minispring.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;

public class MyBeanProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDaoService")) {
            System.out.println("在这里对Bean进行前置修改");
            System.out.println(bean.toString());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDaoService")) {
            System.out.println("在这里对Bean进行后置修改");
            System.out.println(bean.toString());
        }
        return bean;
    }
}
