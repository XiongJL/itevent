package com.liwinon.itevent.entity;

/**
 * 事件环节{1受理中,2-20 处理中,30结束,40回访-满意度调查,50竣工}   ,  或者 关键节点 为负数 -1 -2
 */
public enum StepEnum {
    // 获取为null时,代表在处理中!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //2- 20 处理中
    accept("受理中",1),done("处理结束",30),reVisit("回访用户及满意度调查",40),
    complete("竣工",50);
    private String step;
    private int stepid;

    StepEnum(String step, int stepid) {
        this.step = step;
        this.stepid = stepid;
    }
    //获取指定id的描述
    public static String getStep(int stepid){
        for (StepEnum s : StepEnum.values()){
            if (s.getStepid() == stepid){
                return s.step;
            }
        }
        return null;
    }
    //获取指定描述的id
    public static Integer getId(String step){
        for (StepEnum s : StepEnum.values()){
            if (s.getStep() == step){
                return s.stepid;
            }
        }
        return null;
    }


    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public int getStepid() {
        return stepid;
    }

    public void setStepid(int stepid) {
        this.stepid = stepid;
    }
}
