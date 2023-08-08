package com.lcc.minispring.beans.factory.config;

import com.lcc.minispring.beans.factory.PropertyValues;
import lombok.Getter;

/**
 * Bean定义
 */
public class BeanDefinition {
    public final Class<?> clazz;

    @Getter
    public final PropertyValues propertyValues;

    public BeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> clazz, PropertyValues propertyValues) {
        this.clazz = clazz;
        this.propertyValues = propertyValues;
    }
}
