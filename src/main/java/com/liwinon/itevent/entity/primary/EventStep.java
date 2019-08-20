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
	    private String step;
	    private String executorId;
	    private String executorName;
	    private Date stepDate;
	    private String imgurl;
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
		public String getStep() {
			return step;
		}
		public void setStep(String step) {
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
