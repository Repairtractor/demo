package com.lcc.minispring.context;

import org.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 启动容器，整个容器都是从这里开始启动的
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 关闭容器
     */
    void close();

    void registerShutdownHook();

}
