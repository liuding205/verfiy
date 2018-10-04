package com.bdyh.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by BD-PC11 on 2018/9/5.
 */

public class LoginForm {
    @ApiModelProperty(name = "用户名")
    String account;
    @ApiModelProperty(name = "密码")
    String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
