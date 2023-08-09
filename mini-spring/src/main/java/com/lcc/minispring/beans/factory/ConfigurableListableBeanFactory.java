package com.lcc.minispring.beans.factory;


import com.lcc.minispring.beans.factory.config.AutowireCapableBeanFactory;
import com.lcc.minispring.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ConfigurableBeanFactory,ListableBeanFactory {

}
