package com.joker.usercenter.service;
import java.util.Date;

import com.joker.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("dogYupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://img1.baidu.com/it/u=1645832847,2375824523&fm=253&fmt=auto&app=138&f=JPEG?w=480&h=480");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setEmail("123");
        user.setPhone("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }



    @Test
    void userRegister() {
            String userAccount = "yupi";
            String userPassword = "";
            String checkPassword = "123456";
            String planetCode = "1";

            long result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
            Assertions.assertEquals(-1, result);

            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertEquals(-1, result);

            userAccount = "yupi";
            userPassword = "123456";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertEquals(-1, result);

            userAccount = "yu pi";
            userPassword = "12345678";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertEquals(-1, result);
            checkPassword = "123456789";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertEquals(-1, result);

            userAccount = "dogyupi";
            checkPassword = "12345678";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertEquals(-1, result);

            userAccount = "yupi";
            result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
            Assertions.assertTrue(result > 0);
        }
}