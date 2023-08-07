package com.lcc.minispring.ioc.support;

import com.lcc.minispring.ioc.config.BeanDefinition;

/**
 * Bean工厂
 */
public interface BeanFactory {

    Object getBean(String name);
}
