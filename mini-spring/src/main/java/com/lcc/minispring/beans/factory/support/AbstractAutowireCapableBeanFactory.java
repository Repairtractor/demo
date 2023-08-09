package com.lcc.minispring.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.lcc.minispring.beans.factory.InitializingBean;
import com.lcc.minispring.beans.factory.aware.Aware;
import com.lcc.minispring.beans.factory.aware.BeanClassLoaderAware;
import com.lcc.minispring.beans.factory.aware.BeanFactoryAware;
import com.lcc.minispring.beans.factory.aware.BeanNameAware;
import com.lcc.minispring.beans.factory.config.*;
import com.lcc.minispring.beans.PropertyValue;
import com.lcc.minispring.beans.PropertyValues;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy;


    @SneakyThrows
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        return createBean(beanName, beanDefinition, null);
    }

    @SneakyThrows
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition)  {
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }


        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    private ClassLoader getBeanClassLoader() {
        return this.getClass().getClassLoader();
    }


    @SneakyThrows
    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        // 2. 配置信息 init-method {判断是为了避免二次执行销毁}
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = beanDefinition.clazz.getMethod(initMethodName);
            initMethod.invoke(wrappedBean);
        }
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

        // 判断 SCOPE_SINGLETON、SCOPE_PROTOTYPE
        if (beanDefinition.isSingleton()) {
            registerSingleton(beanName, bean);
        }

        // 注册实现了 DisposableBean 接口的 Bean 对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        return getBean(beanName);
    }

    /**
     * 将bean包装为适配器 注册到关闭钩子中
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 非 Singleton 类型的 Bean 不执行销毁方法
        if (!beanDefinition.isSingleton()) return;

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
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
