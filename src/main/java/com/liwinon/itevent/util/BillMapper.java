package com.liwinon.itevent.util;

/**
 * 对应数据在Excel模板中的坐标
 */
public enum BillMapper {

    EVENTID(4,2),
    EVENTTYPE(5,2),
    EVENTDATE(4,6),
    BILLDATE(5,6),
    ITEMINDEX1(8,2),
    ITEMID1(8,3),
    ITEMTYPE1(8,4),
    ITEMBRAND1(8,5),
    ITEMUNIT1(8,6),
    ITEMCOUNT1(8,7);
    private int x;
    private int y;
    BillMapper(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
