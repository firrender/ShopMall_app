package com.shopmall.dry.shopmall.bean;

/**
 * Created by DrY on 2017/7/20.
 */

public class Banner extends BaseBean {
    private String name;
    private String imgUrl;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
