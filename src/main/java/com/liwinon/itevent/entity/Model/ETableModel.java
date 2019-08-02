package com.liwinon.itevent.entity.Model;

import java.util.Date;

public class ETableModel {
    private String uuid;
    private String event;
    private String itemid;
    private int count;
    private String unit;
    private String userid;  //提单人工号
    private String phone;
    private String adminuser;  //操作人账号
    private String date;
    private String applydate;  //用户申请时间
    private String oaid; //OA单单号
    private String orderid; //请购单单号

    public ETableModel(String uuid, String event, String itemid, int count, String unit, String userid, String phone, String adminuser, String date, String applydate, String oaid, String orderid) {
        this.uuid = uuid;
        this.event = event;
        this.itemid = itemid;
        this.count = count;
        this.unit = unit;
        this.userid = userid;
        this.phone = phone;
        this.adminuser = adminuser;
        this.date = date;
        this.applydate = applydate;
        this.oaid = oaid;
        this.orderid = orderid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAdminuser() {
        return adminuser;
    }

    public void setAdminuser(String adminuser) {
        this.adminuser = adminuser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public String getOaid() {
        return oaid;
    }

    public void setOaid(String oaid) {
        this.oaid = oaid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
