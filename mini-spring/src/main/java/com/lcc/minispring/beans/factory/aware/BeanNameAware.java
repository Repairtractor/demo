package com.lcc.minispring.beans.factory.aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}
