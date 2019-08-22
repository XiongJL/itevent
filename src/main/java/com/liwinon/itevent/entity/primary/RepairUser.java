package com.liwinon.itevent.entity.primary;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITE_RepairUser")
public class RepairUser {
	 	@Id
	    private String userid;  //工号
	    private String wxid;	//企业微信账号
	    private String name;	//姓名
	    private int userlevel;//级别
	    private String phone;    //电话
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getWxid() {
			return wxid;
		}
		public void setWxid(String wxid) {
			this.wxid = wxid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getUserlevel() {
			return userlevel;
		}
		public void setUserlevel(int userlevel) {
			this.userlevel = userlevel;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		@Override
		public String toString() {
			return "RepairUser [userid=" + userid + ", wxid=" + wxid + ", name=" + name + ", userlevel=" + userlevel
					+ ", phone=" + phone + "]";
		}
	    
}
