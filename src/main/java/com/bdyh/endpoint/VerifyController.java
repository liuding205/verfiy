package com.bdyh.endpoint;

import com.bdyh.entity.Coordinates;
import com.bdyh.entity.LoginForm;
import com.bdyh.provider.DragImageProvider;
import com.bdyh.service.DragImageProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author zhaochuanzhen
 * @desc 验证码控制器
 * @since 14:51 2018/9/19
 */
@RestController
public class VerifyController {

    @Autowired
    DragImageProvider dragImageProvider;

    @Autowired
    DragImageProviderService dragImageProviderService;

    /**
     * 拖动图片验证码
     *
     * @param loginForm 密钥，安全校验
     * @return 拖动图片的URL
     */
    @RequestMapping("/dragImage")
    public Map<String, Object> dragImage(LoginForm loginForm,HttpServletRequest request) throws Exception {
        return dragImageProviderService.getVerify(loginForm,request);
    }

    /**
     * 拖动图片验证码
     *
     * @param request 密钥，安全校验
     * @return 拖动图片的URL
     */
    @RequestMapping("/dragImageValidation")
    public Map<String, Object> dragImageValidation(Coordinates coordinates,HttpServletRequest request) throws Exception {
        return dragImageProvider.dragImageValidation(coordinates,request);
    }
}
