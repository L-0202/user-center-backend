package com.joker.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.usercenter.common.BaseResponse;
import com.joker.usercenter.common.ErrorCode;
import com.joker.usercenter.common.ResultUtils;
import com.joker.usercenter.exception.BusinessException;
import com.joker.usercenter.model.domain.User;
import com.joker.usercenter.model.domain.request.UserLoginRequest;
import com.joker.usercenter.model.domain.request.UserRegisterRequest;
import com.joker.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.joker.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.joker.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            //return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User result = userService.userLogin(userAccount, userPassword,request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList =  userService.list(queryWrapper);
        List list = userList.stream().map(this::apply).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 鉴权，是否为管理员
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        //鉴权，仅管理员可删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;
    }

    private User apply(User user) {
        user.setUserPassword(null);
        return userService.getSafetyUser(user);
    }
}
