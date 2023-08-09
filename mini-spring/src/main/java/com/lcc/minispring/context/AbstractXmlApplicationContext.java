package com.lcc.minispring.context;

import com.lcc.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.lcc.minispring.beans.factory.support.DefaultListableBeanFactory;
import lombok.SneakyThrows;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @SneakyThrows
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();

}
