package com.ccl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.ccl.*"})
@MapperScan("com.ccl.mapper")
public class SpringBootWxJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWxJavaApplication.class, args);
    }

}
