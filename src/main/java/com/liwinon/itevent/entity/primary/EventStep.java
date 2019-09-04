package com.liwinon.itevent.entity.primary;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITE_EventStep")
public class EventStep {
	 	@Id
	 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int stepId;
	    private String uuid;
	    private int step;
	    private String executorId;
	    private String executorName;
	    private Date stepDate;
	    private String imgurl;
	    private String remark; //处理人员对事件进行描述
		private String assetsid; //资产牌
		private String location; //位置

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAssetsid() {
		return assetsid;
	}

	public void setAssetsid(String assetsid) {
		this.assetsid = assetsid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStepId() {
			return stepId;
		}
		public void setStepId(int stepId) {
			this.stepId = stepId;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public int getStep() {
			return step;
		}
		public void setStep(int step) {
			this.step = step;
		}
		public String getExecutorId() {
			return executorId;
		}
		public void setExecutorId(String executorId) {
			this.executorId = executorId;
		}
		public String getExecutorName() {
			return executorName;
		}
		public void setExecutorName(String executorName) {
			this.executorName = executorName;
		}
		public Date getStepDate() {
			return stepDate;
		}
		public void setStepDate(Date stepDate) {
			this.stepDate = stepDate;
		}
		public String getImgurl() {
			return imgurl;
		}
		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}

	@Override
	public String toString() {
		return "EventStep{" +
				"stepId=" + stepId +
				", uuid='" + uuid + '\'' +
				", step=" + step +
				", executorId='" + executorId + '\'' +
				", executorName='" + executorName + '\'' +
				", stepDate=" + stepDate +
				", imgurl='" + imgurl + '\'' +
				", remark='" + remark + '\'' +
				", assetsid='" + assetsid + '\'' +
				", location='" + location + '\'' +
				'}';
	}
}
