package com.lcc.minispring.context;

import com.lcc.minispring.beans.factory.aware.Aware;
import org.springframework.beans.BeansException;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws Exception;

}
