package com.liwinon.itevent.entity;

/**
 *  查询退还页面的  用户正在使用中的资产javaBean
 */
public class BackModel {
    //使用人工号
    private String userid;
    //资产序号
    private int aid;
    //资产牌
    private String assetsid;
    private String type;
    private String brand;
    private String unit;

    public BackModel() {
    }

    public BackModel(String userid, int aid, String assetsid, String type, String brand, String unit) {
        this.userid = userid;
        this.aid = aid;
        this.assetsid = assetsid;
        this.type = type;
        this.brand = brand;
        this.unit = unit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAssetsid() {
        return assetsid;
    }

    public void setAssetsid(String assetsid) {
        this.assetsid = assetsid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
