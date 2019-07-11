package com.liwinon.itevent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ITE_Event")
public class Event {
    @Id
    private String uuid;
    private int event;
    private String itemid;
    private int count;
    private String unit;
    private String userid;  //提单人工号
    private String adminuser;  //操作人账号
    private Date date;
    private String remark;

    public Event() {
    }

    public Event(String uuid, int event, String itemid, int count, String unit, String userid, String adminuser, Date date, String remark) {
        this.uuid = uuid;
        this.event = event;
        this.itemid = itemid;
        this.count = count;
        this.unit = unit;
        this.userid = userid;
        this.adminuser = adminuser;
        this.date = date;
        this.remark = remark;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getAdminuser() {
        return adminuser;
    }

    public void setAdminuser(String adminuser) {
        this.adminuser = adminuser;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Event{" +
                "uuid='" + uuid + '\'' +
                ", event=" + event +
                ", itemid='" + itemid + '\'' +
                ", count=" + count +
                ", unit='" + unit + '\'' +
                ", userid='" + userid + '\'' +
                ", adminuser='" + adminuser + '\'' +
                ", date=" + date +
                ", remark='" + remark + '\'' +
                '}';
    }
}
