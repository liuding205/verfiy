package com.bdyh.service;

import com.bdyh.entity.LoginForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author BD-PC40
 * @Title: UserService
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/19 18:26
 */
public interface UserService {
    Map<String,Object> loginPost(LoginForm loginForm, HttpServletRequest request) throws Exception;

    Map getUserStatus(HttpServletRequest request);
}
