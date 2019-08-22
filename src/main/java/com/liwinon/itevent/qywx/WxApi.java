package com.liwinon.itevent.qywx;

public interface WxApi {

     //给IT服务发送卡片消息
     String sendCardToIT(String[] userid, String title, String description, String URL, String btntxt);
     //给IT服务发送任务消息(主要发送给处理人员)
    String sendMissionToIT(String[] userid, String title,
    		String description, String task_id,
            String[] btnKey, String[] btnName,
            String[] btnReplace_name,String URL);
}
