package com.lcc.minispring;

import com.lcc.minispring.ioc.config.BeanDefinition;
import com.lcc.minispring.ioc.support.BeanFactory;
import com.lcc.minispring.ioc.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiniSpringApplicationTests {

    @Test
    void contextLoads() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("user",new BeanDefinition(User.class));
        User user = (User) beanFactory.getBean("user");
        user.say();

    }

}
