package com.liwinon.itevent.entity.primary;

import javax.persistence.*;

@Entity
@Table(name = "ITE_Score")
public class Score {
    @Id
    private String uuid; //事件号
    private String userid;  //工号
    private String qyid;    //企业微信id
    private String phone;    //电话
    private String score;    //打分
    private String remark;    //评论
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
	public String getQyid() {
		return qyid;
	}
	public void setQyid(String qyid) {
		this.qyid = qyid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "Score [uuid=" + uuid + ", userid=" + userid + ", qyid=" + qyid + ", phone=" + phone + ", score=" + score
				+ ", remark=" + remark + "]";
	}
    
}
