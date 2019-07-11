package com.liwinon.itevent.entity;

import javax.persistence.*;

@Entity
@Table(name = "ITE_Access")
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int aid;  //资产序号
    private String eventid;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
