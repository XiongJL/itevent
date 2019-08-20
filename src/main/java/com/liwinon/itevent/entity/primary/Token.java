package com.liwinon.itevent.entity.primary;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 企业微信token 实体类
 */
@Entity
@Table(name = "ITE_Token")
public class Token {
    @Id
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date invalidTime; //失效日期  =   now（）+effectiveTime
    private int effectiveTime; //有效时长 单位/S
    private String token;
    private int type; //token类型, 0 普通 , 1 通讯录.

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
