package com.ncf.apollodemo;

import com.ncf.apollodemo.config.DingTalkConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@MapperScan("com.ncf.apollodemo.dao")
public class ApolloDemoApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context  = SpringApplication.run(ApolloDemoApplication.class, args);
        //注册DingTalk Stream长连接
        context.getBean(DingTalkConfig.class).initDingTalkConnection();

    }

}
