package com.liwinon.itevent.entity.Model;

import java.util.Arrays;
import java.util.Date;

public class DetailsModel {
	private String uuid; //事件id
	private Date date; //申请人姓名
	private String username; //申请人姓名
	private String userid; //申请人工号
	private String phone;   //申请人电话
	private String state;   //事件状态
	private String type; //事件类型
	private String description; //事件描述
	private String itemid; //维修/帮助的物料编码
	private String remark; //用户描述
	private String adminuser; //填单人工号
    private String[] imgurl; //图片路径
	
    
	@Override
	public String toString() {
		return "DetailsModel [uuid="+uuid+"date="+date+"username=" + username + ", userid=" + userid + ", phone=" + phone + ", state=" + state
				+ ", type=" + type + ", description=" + description + ", itemid=" + itemid + ", remark=" + remark
				+ ", adminuser=" + adminuser + ", imgurl=" + Arrays.toString(imgurl) + "]";
	}
	public DetailsModel(String uuid,Date date,String username, String userid, String phone, String state, String type, String description,
			String itemid, String remark, String adminuser, String[] imgurl) {
		this.uuid = uuid;
		this.date = date;
		this.username = username;
		this.userid = userid;
		this.phone = phone;
		this.state = state;
		this.type = type;
		this.description = description;
		this.itemid = itemid;
		this.remark = remark;
		this.adminuser = adminuser;
		this.imgurl = imgurl;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdminuser() {
		return adminuser;
	}
	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}
	public String[] getImgurl() {
		return imgurl;
	}
	public void setImgurl(String[] imgurl) {
		this.imgurl = imgurl;
	}
    
}
