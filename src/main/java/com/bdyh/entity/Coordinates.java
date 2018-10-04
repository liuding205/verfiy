package com.bdyh.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author BD-PC40
 * @Title: Coordinates
 * @ProjectName bdyh-model-server
 * @Description: TODO
 * @date 2018/9/20 17:55
 */
public class Coordinates {

    @ApiModelProperty(name = "x轴")
    Integer axisX;
    @ApiModelProperty(name = "y轴")
    Integer axisY;

    public Integer getAxisX() {
        return axisX;
    }

    public void setAxisX(Integer axisX) {
        this.axisX = axisX;
    }

    public Integer getAxisY() {
        return axisY;
    }

    public void setAxisY(Integer axisY) {
        this.axisY = axisY;
    }
}
