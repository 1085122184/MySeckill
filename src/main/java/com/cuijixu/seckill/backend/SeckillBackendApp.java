package com.cuijixu.seckill.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cuijixu.seckill.backend.dao")
public class SeckillBackendApp {

    public static void main(String[] args) {
        SpringApplication.run(SeckillBackendApp.class, args);
    }
}

