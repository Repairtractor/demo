package com.lcc.minispring.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.lcc.minispring.beans.factory.AutowireCapableBeanFactory;
import com.lcc.minispring.beans.factory.PropertyValue;
import com.lcc.minispring.beans.factory.PropertyValues;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.config.BeanReference;
import com.lcc.minispring.beans.factory.processor.BeanPostProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy;


    @SneakyThrows
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {

        return createBean(beanName, beanDefinition, null);

    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }


    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
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
        Object bean = createInstance(beanName, beanDefinition, args);

        //这里通过bean定义的属性进行注入
        applyPropertyValues(beanName, bean, beanDefinition);

        bean = initializeBean(beanName, bean, beanDefinition);

        registerSingleton(beanName, bean);
        return getBean(beanName);
    }

    @SneakyThrows
    private Object createInstance(String beanName, BeanDefinition beanDefinition, Object... args) {
        if (args == null || args.length == 0) {
            return beanDefinition.clazz.newInstance();
        }
        Class<?> clazz = beanDefinition.clazz;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        Constructor<?> noSuchConstructor = Arrays.stream(declaredConstructors)
                .filter(constructor -> isAllArgsMatch(constructor.getParameterTypes(), args))
                .findAny()
                .orElseThrow(() -> new RuntimeException("no such constructor"));
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, noSuchConstructor, args);
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
        return instantiationStrategy == null ? new CglibSubclassingInstantiationStrategy() : instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

}
