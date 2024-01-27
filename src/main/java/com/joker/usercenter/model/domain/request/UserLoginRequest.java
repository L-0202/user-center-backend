package com.joker.usercenter.model.domain.request;

import lombok.Data;

/**
 * 用户登录
 */
@Data
public class UserLoginRequest {

    private static final long serialVersionUID = 3636068235075360278L;

    private String userAccount;
    private String userPassword;

}
