package com.lcc.minispring.beans.factory.context;

import com.lcc.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.BeansException;

import java.util.Map;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }


}
