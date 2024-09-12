package com.tatakai.parrotojbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tatakai.parrotojbackend.mapper")
public class ParrotOjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParrotOjBackendApplication.class, args);
    }

}
