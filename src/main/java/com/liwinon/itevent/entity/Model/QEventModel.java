package com.liwinon.itevent.entity.Model;

public class QEventModel {
    private String uuid;
    private String type;
    private String state;
    private String applyDate;
    private String stepDate;
    private String step;
    private String executorName;
    private String phone; //处理人电话

    public QEventModel(){};
    public QEventModel(String uuid, String type, String state, String applyDate, String stepDate, String step, String executorName, String phone) {
        this.uuid = uuid;
        this.type = type;
        this.state = state;
        this.applyDate = applyDate;
        this.stepDate = stepDate;
        this.step = step;
        this.executorName = executorName;
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getStepDate() {
        return stepDate;
    }

    public void setStepDate(String stepDate) {
        this.stepDate = stepDate;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "QEventModel{" +
                "uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", stepDate='" + stepDate + '\'' +
                ", step='" + step + '\'' +
                ", executorName='" + executorName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
