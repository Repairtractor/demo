package com.lcc.minispring.beans.factory.config;

import com.lcc.minispring.beans.PropertyValues;
import lombok.Data;
import lombok.Getter;

@Data
/**
 * Bean定义
 */
public class BeanDefinition {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;


    public final Class<?> clazz;

    @Getter
    public final PropertyValues propertyValues;

    private  String initMethodName;

    private  String destroyMethodName;

    public BeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> clazz, PropertyValues propertyValues) {
        this.clazz = clazz;
        this.propertyValues = propertyValues;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || scope == null;
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }
}
