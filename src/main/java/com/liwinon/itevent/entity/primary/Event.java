package com.liwinon.itevent.entity.primary;

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
    private String qyid;  //提单人企业微信号
    private String phone;  //联系方式
    private String adminuser;  //操作人账号
    private Date date;
    private Date applydate;  //用户申请时间
    private String oaid; //OA单单号
    private String orderid; //请购单单号
    private String remark;
    private String state;//事件状态

    public String getQyid() {
        return qyid;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public Event() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Event(String uuid, int event, String itemid, int count, String unit, String userid, String qyid, String phone, String adminuser, Date date, Date applydate, String oaid, String orderid, String remark, String state) {
        this.uuid = uuid;
        this.event = event;
        this.itemid = itemid;
        this.count = count;
        this.unit = unit;
        this.userid = userid;
        this.qyid = qyid;
        this.phone = phone;
        this.adminuser = adminuser;
        this.date = date;
        this.applydate = applydate;
        this.oaid = oaid;
        this.orderid = orderid;
        this.remark = remark;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
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
                ", qyid='" + qyid + '\'' +
                ", phone='" + phone + '\'' +
                ", adminuser='" + adminuser + '\'' +
                ", date=" + date +
                ", applydate=" + applydate +
                ", oaid='" + oaid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
