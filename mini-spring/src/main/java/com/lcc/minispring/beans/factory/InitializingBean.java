package com.lcc.minispring.beans.factory;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
