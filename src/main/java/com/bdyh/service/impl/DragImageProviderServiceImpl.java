package com.bdyh.service.impl;

import com.bdyh.common.Constants;
import com.bdyh.common.SpringControllerContext;
import com.bdyh.common.utils.VerifyImageUtil;
import com.bdyh.entity.LoginForm;
import com.bdyh.properties.SystemProperties;
import com.bdyh.provider.DragImageProvider;
import com.bdyh.service.DragImageProviderService;
import com.bdyh.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author BD-PC40
 * @Title: DragImageProviderServiceImpl
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/2112:58
 */
@Service
public class DragImageProviderServiceImpl implements DragImageProviderService {

    private static UserService userService;
    private static SystemProperties systemProperties;

    @Autowired
    public DragImageProviderServiceImpl(SystemProperties systemProperties,UserService userService) {
        DragImageProviderServiceImpl.systemProperties = systemProperties;
        DragImageProviderServiceImpl.userService = userService;
    }

    @Override
    public Map<String, Object> getVerify(LoginForm loginForm, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>(4);
        result.put(Constants.RESULT, Constants.RESULT_FAILED);
        Map<String, Object> map = userService.loginPost(loginForm, request);
        if("LOGIN_USER_SUCCESS".equals(map.get("result"))){

            Map<String, byte[]> pictureMap;
            File templateFile;  //模板图片
            File targetFile;  //
            Random random = new Random();
            int templateNo = 1;
            int targetNo = random.nextInt(20) + 1;
//xiao
            templateFile =new File(systemProperties.getPath().getPictureSourceSm() + templateNo + ".png");
//da
            targetFile =new File(systemProperties.getPath().getPictureSourceMax() + targetNo + ".jpg");
            pictureMap = VerifyImageUtil.pictureTemplatesCut(templateFile, targetFile, "png", "jpg");
            byte[] oriCopyImages = pictureMap.get("oriCopyImage");
            byte[] newImages = pictureMap.get("newImage");
            int x = VerifyImageUtil.getX();
            int y = VerifyImageUtil.getY();
            //生成移动图
            String path1 = UUID.randomUUID().toString()
                    .replaceAll("_", "")
                    .replaceAll("-", "");

            String path2 = UUID.randomUUID().toString()
                    .replaceAll("_", "")
                    .replaceAll("-", "");
            FileOutputStream fout = new FileOutputStream(systemProperties.getPath().getPictureOutput() + path1 + ".png");
            //将字节写入文件
            try {
                fout.write(oriCopyImages);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fout.close();

            FileOutputStream newImageFout = new FileOutputStream(systemProperties.getPath().getPictureOutput() + path2 + ".png");

            //将字节写入文件
            newImageFout.write(newImages);
            newImageFout.close();

            String path = systemProperties.getPath().getUrl();
            result.put("path1", path + path1 + ".png");
            result.put("path2", path + path2 + ".png");

            //向session中
            Map<String, Integer> verify_code = new HashMap<>(4);
            verify_code.put("axisX",x);
            verify_code.put("axisY",y);
            SpringControllerContext.getContext().setSessionValue(Constants.VERIFY_CODE, verify_code, request);
            result.put(Constants.RESULT,Constants.RESULT_SUCCESS);
        }else{

            return map;
        }

        return result;
    }
}
