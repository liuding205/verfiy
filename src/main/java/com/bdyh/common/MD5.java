package com.bdyh.common;

/**
 * Created by BD-PC11 on 2017/4/7.
 */

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class MD5 {
    //密钥生成器
    private static KeyGenerator keyGenerator = null;

    //对称密钥
    private static SecretKey secretKey = null;

    //加解密时的初始化向量must be 8 bytes long
    private static IvParameterSpec ivParameterSpec = null;

    //Cipher,加解密主体实例
    private static Cipher cipher = null;

    private final static void init() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException {
        keyGenerator = KeyGenerator.getInstance("DES", "SunJCE");
        DESKeySpec desKeySpec = new DESKeySpec(encrypt("bdyh").getBytes("UTF-8"));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        secretKey = secretKeyFactory.generateSecret(desKeySpec);
        ivParameterSpec = new IvParameterSpec(encrypt("bdyh").substring(0, 8).getBytes("UTF-8"));
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding", "SunJCE");//DES加密算法，CBC的反馈模
    }

    static {
        try {
            init();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public final static String  getMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String encrypt(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String encrypt(String src, String mode) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        //初始化-- ENCRYPT_MODE：加密模式， key：密钥，iv：初始化向量
        if (src == null) {
        }
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] srcByte = src.getBytes();
        //加密
        byte[] targetByte = cipher.doFinal(srcByte);
        //Base64编码
        String targetString = new BASE64Encoder().encode(targetByte);
        return targetString;
    }

    public final static String decrypt(String des, String mode) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
        //初始化-- DECRYPT_MODE：解密模式， key：密钥，iv：初始化向量
        if (des == null) {
        }
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        //Base64解码
        byte[] srcByte = new BASE64Decoder().decodeBuffer(des.startsWith("\"") && des.endsWith("\"") ? des.substring(1, des.length()) : des);
        //解密
        byte[] targetByte = cipher.doFinal(srcByte);
        return new String(targetByte);
    }
}

