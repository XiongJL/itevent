package com.liwinon.itevent.entity.Model;


public class ATableModel {

    private int aid;
    private String assetsid;
    private String username;
    private String userid;
    private String phone;
    private String type;
    private String brand;
    private String state;
    private String store;
    private String buyDate;

    public ATableModel() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ATableModel(int aid, String assetsid, String username, String userid, String phone, String type, String brand, String state, String store, String buyDate) {
        this.aid = aid;
        this.assetsid = assetsid;
        this.username = username;
        this.userid = userid;
        this.phone = phone;
        this.type = type;
        this.brand = brand;
        this.state = state;
        this.store = store;
        this.buyDate = buyDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }
}
