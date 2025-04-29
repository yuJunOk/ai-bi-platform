package com.example.abs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pengYuJun
 */
@MapperScan("com.example.abs.mapper")
@SpringBootApplication
public class AiBiSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiBiSystemApplication.class, args);
    }

}
