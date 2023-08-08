package com.lcc.minispring.beans.factory.reader;

import com.lcc.minispring.beans.factory.support.BeanDefinitionRegistry;
import com.lcc.minispring.core.io.DefaultResourceLoader;
import com.lcc.minispring.core.io.ResourceLoader;
import lombok.Getter;

/**
 * 将读取到的BeanDefinition注册到BeanDefinitionRegistry中
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    @Getter
    private BeanDefinitionRegistry registry;

    @Getter
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry,new DefaultResourceLoader());
    }



}
