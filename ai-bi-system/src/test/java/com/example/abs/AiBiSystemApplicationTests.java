package com.example.abs;

import com.example.abs.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiBiSystemApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        System.out.println(userService.getById(1));
    }

}
