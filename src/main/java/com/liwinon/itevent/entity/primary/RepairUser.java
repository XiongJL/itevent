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
	    private String userid;  //企业微信userid
	    private String personid;	//工号
	    private String name;	//姓名
	    private int userlevel;//级别
	    private String phone;    //电话
		private String team; //组别
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getPersonid() {
			return personid;
		}
		public void setPersonid(String personid) {
			this.personid = personid;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "RepairUser{" +
				"userid='" + userid + '\'' +
				", personid='" + personid + '\'' +
				", name='" + name + '\'' +
				", userlevel=" + userlevel +
				", phone='" + phone + '\'' +
				", team='" + team + '\'' +
				'}';
	}
}
