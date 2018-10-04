package com.bdyh.common.utils;

import com.bdyh.common.MD5;

/**
 * @author BD-PC40
 * @Title: TestUtils
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/20 11:42
 */
public class TestUtils {

    public static void main(String[] args){
        String bdyhfbzx = MD5.getMd5("bdyhfbzx");
        System.out.println(bdyhfbzx);
    }
}
