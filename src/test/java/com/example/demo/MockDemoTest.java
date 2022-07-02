package com.example.demo;

import com.example.demo.service.user.UserService;
import com.example.demo.vo.user.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockDemoTest {

    //先普通的注入一个userService bean
    @Autowired
    private UserService userService;

    @Test
    public void getUserById() {
        //普通的使用userService，他里面会再去调用userDao取得数据库的数据
        UserVO user = userService.getUser(1);

        //检查结果
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), Integer.valueOf(1));
        Assertions.assertEquals(user.getUsername(), "asd");
    }

}