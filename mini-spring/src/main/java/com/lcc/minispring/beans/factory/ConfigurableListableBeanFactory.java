package com.lcc.minispring.beans.factory;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ConfigurableBeanFactory,ListableBeanFactory {

}
