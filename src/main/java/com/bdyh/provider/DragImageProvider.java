package com.bdyh.provider;

import com.bdyh.common.Constants;
import com.bdyh.common.SpringControllerContext;
import com.bdyh.entity.Coordinates;
import com.bdyh.entity.LoginForm;
import com.bdyh.properties.SystemProperties;
import com.bdyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author zhaochuanzhen
 * @desc 拖动图片提供者
 * @since 14:54 2018/9/19
 */
@Component
public class DragImageProvider {

    private static UserService userService;
    private static SystemProperties systemProperties;

    @Autowired
    public DragImageProvider(SystemProperties systemProperties,UserService userService) {
        DragImageProvider.systemProperties = systemProperties;
        DragImageProvider.userService = userService;
    }

    public static void cut(int x, int y, int width, int height, String srcpath, String subpath) throws IOException {//裁剪方法
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            is = new FileInputStream(srcpath); //读取原始图片
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg"); //ImageReader声称能够解码指定格式
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is); //获取图片流
            reader.setInput(iis, true); //将iis标记为true（只向前搜索）意味着包含在输入源中的图像将只按顺序读取
            ImageReadParam param = reader.getDefaultReadParam(); //指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            Rectangle rect = new Rectangle(x, y, width, height); //定义空间中的一个区域
            param.setSourceRegion(rect); //提供一个 BufferedImage，将其用作解码像素数据的目标。
            BufferedImage bi = reader.read(0, param); //读取索引imageIndex指定的对象
            ImageIO.write(bi, "jpg", new File(subpath)); //保存新图片
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    private static void cutByTemplate2(BufferedImage oriImage, BufferedImage newSrc, BufferedImage newSrc2, int x, int y, int width, int height) {
        //固定圆半径为5
        int c_r = 20;
        double rr = Math.pow(c_r, 2);//r平方
        //圆心的位置
        Random rand = new Random();
        int c_a = rand.nextInt(width - 2 * c_r) + (x - c_r);//x+c_r+10;//圆心x坐标必须在(x+r,x+with-r)范围内
        //System.out.println(c_a);
        int c_b = y;

        //第二个圆（排除圆内的点）
        Random rand2 = new Random();
        int c2_a = x;
        int c2_b = rand2.nextInt(height - 2 * c_r) + (y + c_r);//y+c_r+50;//圆心y坐标必须在(y+r,y+height-r)范围内

        //System.out.println(oriImage.getWidth()+"   "+oriImage.getHeight());
        for (int i = 0; i < oriImage.getWidth(); i++) {
            for (int j = 0; j < oriImage.getHeight(); j++) {
//                data[i][j]=oriImage.getRGB(i,j);

//                (x-a)²+(y-b)²=r²中，有三个参数a、b、r，即圆心坐标为(a，b)，半径r。
                double f = Math.pow((i - c_a), 2) + Math.pow((j - c_b), 2);

                double f2 = Math.pow((i - c2_a), 2) + Math.pow((j - c2_b), 2);

                int rgb = oriImage.getRGB(i, j);
                if (i >= x && i < (x + width) && j >= y && j < (y + height) && f2 >= rr) {//在矩形内
                    //块范围内的值
                    in(newSrc, newSrc2, i - x, j - y, rgb);
                }/* else if (f <= rr) {
                    //在圆内
//                   in(newSrc, newSrc2, i, j, rgb);
                } */ else {
                    //剩余位置设置成透明
                    out(newSrc, newSrc2, i, j, rgb);
                }

            }
        }

    }

    public static void main(String[] args) throws Exception {
        //ImageUtil tt = new ImageUtil();
        //图片必须是png格式，因为需要做透明背景
        //原图
        Random rand = new Random();

        BufferedImage src = ImageIO.read(new File(systemProperties.getPath().getPictureSourceMax() + "00" + (rand.nextInt(3) + 1) + ".jpg"));
        //移动图
        BufferedImage newSrc = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        //对比图
        BufferedImage newSrc2 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage

        //抠块的大小
        int blockWidth = 100;
        int blockHeight = 100;

        Random rand1 = new Random();
        int x = rand1.nextInt(src.getWidth() - blockWidth - 20) + 20;//10,width-200

        Random rand2 = new Random();
        int y = rand2.nextInt(src.getHeight() - blockHeight - 20) + 20;//
        cutByTemplate2(src, newSrc, newSrc2, x, y, blockWidth, blockHeight);//图片大小是固定，位置是随机

        //生成移动图
        String path1 = UUID.randomUUID().toString()
                .replaceAll("_", "")
                .replaceAll("-", "");

        String path2 = UUID.randomUUID().toString()
                .replaceAll("_", "")
                .replaceAll("-", "");

        ImageIO.write(newSrc, "png", new File("F:\\verify\\temp\\" + path1 + ".png"));
        //生成对比图
        ImageIO.write(newSrc2, "png", new File("F:\\verify\\temp\\" + path2 + ".png"));
    }

    private static void in(BufferedImage newSrc, BufferedImage newSrc2, int i, int j, int rgb) {
        newSrc.setRGB(i, j, rgb);
        //原图设置变灰
//        int r = (0xff & rgb);
//        int g = (0xff & (rgb >> 8));
//        int b = (0xff & (rgb >> 16));
//        rgb = r + (g << 8) + (b << 16) + (100 << 24);
        //rgb = r + (g << 8) + (b << 16);
//        newSrc2.setRGB(i, j, rgb);
    }

    private static void out(BufferedImage newSrc, BufferedImage newSrc2, int i, int j, int rgb) {
//        newSrc.setRGB(i, j, 0x00ffffff);
        newSrc2.setRGB(i, j, rgb);
    }

    public Map<String, Object> getVerify(LoginForm loginForm, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>(4);
        result.put(Constants.RESULT, Constants.RESULT_FAILED);
        Map<String, Object> map = userService.loginPost(loginForm, request);
        if("LOGIN_USER_SUCCESS".equals(map.get("result"))){
            //ImageUtil tt = new ImageUtil();
            //图片必须是png格式，因为需要做透明背景
            //原图
            Random rand = new Random();

            BufferedImage src = ImageIO.read(new File(systemProperties.getPath().getPictureSourceMax() + "00" + (rand.nextInt(3) + 1) + ".jpg"));
            //移动图
            BufferedImage newSrc = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
            //对比图
            BufferedImage newSrc2 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage

            //抠块的大小
            int blockWidth = 100;
            int blockHeight = 100;

            Random rand1 = new Random();
            int x = rand1.nextInt(src.getWidth() - blockWidth - 20) + 20;//10,width-200

            Random rand2 = new Random();
            int y = rand2.nextInt(src.getHeight() - blockHeight - 20) + 20;//
            cutByTemplate2(src, newSrc, newSrc2, x, y, blockWidth, blockHeight);//图片大小是固定，位置是随机

            //生成移动图
            String path1 = UUID.randomUUID().toString()
                    .replaceAll("_", "")
                    .replaceAll("-", "");

            String path2 = UUID.randomUUID().toString()
                    .replaceAll("_", "")
                    .replaceAll("-", "");

            ImageIO.write(newSrc, "png", new File(systemProperties.getPath().getPictureOutput() + path1 + ".png"));
            //生成对比图
            ImageIO.write(newSrc2, "png", new File(systemProperties.getPath().getPictureOutput() + path2 + ".png"));

            String path = systemProperties.getPath().getUrl();
            result.put("path1", path + path1 + ".png");
            result.put("path2", path + path2 + ".png");
            result.put(Constants.RESULT,Constants.RESULT_SUCCESS);
            //向session中
            Map<String, Integer> verify_code = new HashMap<>(4);
            verify_code.put("axisX",x);
            verify_code.put("axisY",y);
            SpringControllerContext.getContext().setSessionValue(Constants.VERIFY_CODE, verify_code, request);
        }else{

          return map;
        }

        return result;
    }

    public Map<String,Object> dragImageValidation(Coordinates coordinates,HttpServletRequest request) {
        Map<String, Object> result_verif = new HashMap<String, Object>();
        result_verif.put(Constants.RESULT, Constants.RESULT_FAILED);
        Integer axisX = coordinates.getAxisX();
        Integer axisY = coordinates.getAxisY();
        if(axisX == null || axisY == null){
            return result_verif;
        }
        Map<String, Object> sessionValue = (Map<String, Object>) SpringControllerContext.getContext().getSessionValue(Constants.VERIFY_CODE, request);
        if(!sessionValue.isEmpty()){
            Integer axisX_s = (Integer) sessionValue.get("axisX");
            Integer axisY_s = (Integer) sessionValue.get("axisY");
            if(axisX >(axisX_s +5) || axisX<(axisX_s +5)){
                result_verif.put(Constants.RESULT,Constants.RESULT_SUCCESS);
            }
        }

        return result_verif;
    }
}
