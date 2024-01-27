package com.joker.usercenter.service;

import com.joker.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 李保定
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-01-23 10:48:56
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     * @param userAccount        用户账户
     * @param userPassword       用户密码
     * @param request  用户登录状态
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
