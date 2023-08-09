package com.lcc.minispring;

import com.lcc.minispring.DTO.UserService;
import com.lcc.minispring.beans.factory.config.BeanDefinition;
import com.lcc.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.lcc.minispring.context.ApplicationContext;
import com.lcc.minispring.context.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
class MiniSpringApplicationTests {

    @Test
    void contextLoads() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userService = (UserService) applicationContext.getBean("userDaoService");
        UserService userService1 = (UserService) applicationContext.getBean("userDaoService");
        System.out.println(Objects.equals(userService, userService1));

    }

}
