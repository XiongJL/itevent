package com.liwinon.itevent.entity;

/**
 * 编码与事件对应
 * 事件(01以旧换新，02报废，03新资产入库，04退还,05 借用
 * 06 借用超期出库 07提单领取 08 已有资产(未登记)记录 09 物料登记)
 */
public enum EventEnum {
    EXCHANGE("以旧换新",1),SCRAP("报废",2),NEWAS("新资产入库",3)
    ,BACK("退还",4),BORROW("借用",5),BORROWOUT("借用出库",6)
    ,SUBBILL("提单领取",7),OLDAS("已有资产登记",8),ADDITEM("物料登记",9);

    private String event;
    private int eventid;

    EventEnum(String event, int eventid) {
        this.event = event;
        this.eventid = eventid;
    }

    //自定义方法,获取指定id的事件
    public static String getName(int index){
        for (EventEnum e : EventEnum.values()){
            if (e.getEventid() == index){
                return e.event;
            }
        }
        return null;
    }
    //获取指定事件的id
    public static Integer getId(String event){
        for (EventEnum e : EventEnum.values()){
            if (e.getEvent().equals(event)){
                return e.eventid;
            }
        }
        return null;
    }

    //get,set
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }
}
