package com.lcc.minispring.beans.factory.facotryBean;

public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();

}
