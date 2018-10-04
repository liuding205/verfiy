package com.bdyh.service;

import com.bdyh.entity.LoginForm;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author BD-PC40
 * @Title: DragImageProviderService
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/2112:58
 */
public interface DragImageProviderService {
    Map<String,Object> getVerify(LoginForm loginForm, HttpServletRequest request) throws Exception;
}
