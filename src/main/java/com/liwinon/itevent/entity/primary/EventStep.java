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
	    private int stepId;  //环节id序号
	    private String uuid;  //事件号
	    private int step;  //事件环节{开始1,处理2-5(移交过程2程序员,3工程师,4高工,5经理),结束6,回访7,满意度调查8,竣工0}
	    private String executorId;  //执行人工号
	    private String executorName;  //执行人姓名
	    private Date stepDate;    //环节开始时间
	    private String imgurl;  //图片地址
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
			return "EventStep [stepId=" + stepId + ", uuid=" + uuid + ", step=" + step + ", executorId=" + executorId
					+ ", executorName=" + executorName + ", stepDate=" + stepDate + ", imgurl=" + imgurl + "]";
		}
		
	    
}
