package com.liwinon.itevent.qywx;

public interface WxApi {

    //给IT实践服务发送卡片消息
     String sendCardToIT(String[] userid, String title, String description, String URL, String btntxt);

}
