package com.bdyh.service.impl;

import com.bdyh.common.Constants;
import com.bdyh.common.MD5;
import com.bdyh.common.SpringControllerContext;
import com.bdyh.entity.LoginForm;
import com.bdyh.entity.User;
import com.bdyh.repository.UserRepositoryManagerDsl;
import com.bdyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BD-PC40
 * @Title: UserServiceImpl
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/1918:27
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryManagerDsl userRepositoryManagerDsl;

    @Autowired
    public UserServiceImpl(UserRepositoryManagerDsl userRepositoryManagerDsl) {
        this.userRepositoryManagerDsl = userRepositoryManagerDsl;
    }
    @Override
    public Map<String, Object> loginPost(LoginForm loginForm,HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        if (loginForm.getAccount() == null || loginForm.getPassword() == null) {
            result.put(Constants.RESULT, Constants.ACCOUNT_ERROR);
            return result;
        }else{//验证用户帐号及其密码
            //根据account,检出用户信息,并比对密码
            User user = userRepositoryManagerDsl.findUser(loginForm.getAccount());
            if(user != null){
                if(MD5.getMd5(loginForm.getPassword()).equals(user.getPassword())){
                    SpringControllerContext.getContext().setSessionValue(Constants.LOGIN_SESSION_USER_ID, user.getId(), request);
                    result.put("userid", user.getId());
                    result.put(Constants.RESULT, Constants.LOGIN_USER_SUCCESS);
                    return result;
                }else{
                    result.put(Constants.RESULT, Constants.ACCOUNT_ERROR);
                }

            }else{
                result.put(Constants.RESULT, Constants.ACCOUNT_NOT_EXIST);
            }

        }
        return result;
    }

    @Override
    public Map getUserStatus(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(4);
        result.put(Constants.RESULT, Constants.RESULT_FAILED);

        Long userId = (Long) SpringControllerContext.getContext().getSessionValue(Constants.LOGIN_SESSION_USER_ID, request);
        if (userId == null) {
            result.put(Constants.RESULT, Constants.LOGIN_NEED);
        } else {
            Map<String, Object> map = new HashMap<>(4);
            map.put("userid", userId);
            result.put(Constants.RESULT, Constants.RESULT_SUCCESS);
            result.put("data", map);
        }
        return result;
    }
}
