package com.lcc.minispring.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.lcc.minispring.beans.factory.AutowireCapableBeanFactory;
import com.lcc.minispring.beans.factory.PropertyValue;
import com.lcc.minispring.beans.factory.PropertyValues;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.config.BeanReference;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private  InstantiationStrategy instantiationStrategy;


    @SneakyThrows
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        //这里通过bean定义new bean 然后放入单例池
        registerSingleton(beanName, beanDefinition.clazz.newInstance());
        Object bean = getBean(beanName);
        //这里通过bean定义的属性进行注入
        applyPropertyValues(beanName, bean, beanDefinition);

        bean = initializeBean(beanName, bean, beanDefinition);
        return bean;
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {


        return null;
    }

    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.propertyValues;
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            String name = propertyValue.name;
            Object value = propertyValue.value;

            if (propertyValue.value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) propertyValue.value;
                value = getBean(beanReference.beanName);
            }
            BeanUtil.setFieldValue(bean, name, value);
        }
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        if (args == null || args.length == 0) {
            return createBean(beanName, beanDefinition);
        }
        Class<?> clazz = beanDefinition.clazz;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        Constructor<?> noSuchConstructor = Arrays.stream(declaredConstructors)
                .filter(constructor -> isAllArgsMatch(constructor.getParameterTypes(), args))
                .findAny()
                .orElseThrow(() -> new RuntimeException("no such constructor"));
        Object instantiate = instantiationStrategy.instantiate(beanDefinition, beanName, noSuchConstructor, args);
        registerSingleton(beanName, instantiate);
        return getBean(beanName);
    }

    private boolean isAllArgsMatch(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].isInstance(args[i])) {
                return false;
            }
        }
        return true;
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

}
