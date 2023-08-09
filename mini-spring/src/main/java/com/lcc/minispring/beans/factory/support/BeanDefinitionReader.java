package com.lcc.minispring.beans.factory.support;

import com.lcc.minispring.beans.factory.support.BeanDefinitionRegistry;
import com.lcc.minispring.core.io.Resource;
import com.lcc.minispring.core.io.ResourceLoader;

/**
 * 负责加载资源转换为BeanDefinition
 */
public interface BeanDefinitionReader {

    /**
     * 读取BeanDefinition
     * @param location
     * @throws Exception
     */
    void loadBeanDefinitions(String location) throws Exception;
    void loadBeanDefinitions(String... location) throws Exception;

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws Exception;
    void loadBeanDefinitions(Resource... resource) throws Exception;



}
