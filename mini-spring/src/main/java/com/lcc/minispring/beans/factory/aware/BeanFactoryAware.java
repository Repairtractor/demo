package com.lcc.minispring.beans.factory.aware;

import com.lcc.minispring.beans.factory.BeanFactory;
import org.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware {

   void setBeanFactory(BeanFactory beanFactory) throws Exception;

}
