package com.liwinon.itevent.entity.Model;

import java.util.Date;

public class MissionModel {
    private String uuid ;  //事件编号
    private String type; //事件类型
    private String description; //事件描述
    private String userid; //申请人工号
    private String username; //申请人姓名
    private String phone;   //申请人电话
    private String itemid; //维修/帮助的物料编码
    private Date date ; //申请时间
    private String adminuser; //填单人工号
    private String adminname; //填单人姓名
    private String state;   //事件状态
    private String remark; //用户描述

    public MissionModel() {
    }

    public MissionModel(String uuid, String type, String description, String userid, String username, String phone, String itemid, Date date, String adminuser, String adminname, String state, String remark) {
        this.uuid = uuid;
        this.type = type;
        this.description = description;
        this.userid = userid;
        this.username = username;
        this.phone = phone;
        this.itemid = itemid;
        this.date = date;
        this.adminuser = adminuser;
        this.adminname = adminname;
        this.state = state;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAdminuser() {
        return adminuser;
    }

    public void setAdminuser(String adminuser) {
        this.adminuser = adminuser;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MissionModel{" +
                "uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", itemid='" + itemid + '\'' +
                ", date=" + date +
                ", adminuser='" + adminuser + '\'' +
                ", adminname='" + adminname + '\'' +
                ", state='" + state + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
