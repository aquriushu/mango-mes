package com.mango.bkl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.mango.bkl.mapper")
@ComponentScan(basePackages = {"com.mango.common", "com.mango.bkl"})
public class MangoBKLApplication {
    public static void main(String[] args) {
        SpringApplication.run(MangoBKLApplication.class, args);
    }

}
