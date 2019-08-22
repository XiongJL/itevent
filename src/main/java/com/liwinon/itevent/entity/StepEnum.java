package com.liwinon.itevent.entity;

/**
 * 事件环节{受理中1,处理2-5(移交过程2程序员,3工程师,4高工,5经理),结束6,回访7,满意度调查8,竣工0}
 */
public enum StepEnum {
    accept("受理中",1),handleLevel1("程序员",2),handleLevel2("助理工程师",3),handleLevel3("工程师",4),
    handleLevel4("经理",5),done("处理结束",6),reVisit("回访用户",7),
    survey("用户满意度调查",8),complete("竣工",0);
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
