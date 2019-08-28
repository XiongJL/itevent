package com.liwinon.itevent.qywx;

public interface WxApi {

     //给IT服务发送卡片消息
     String sendCardToIT(String[] userid, String title, String description, String URL, String btntxt);

     //给IT服务发送任务消息(主要发送给处理人员)
    String sendMissionToIT(String[] userid, String title,
    		String description, String task_id,
            String[] btnKey, String[] btnName,
            String[] btnReplace_name,String URL);
    //给某个用户发送文本消息
    String sendTextToOne(String[] userid,String content);


    //更新任务卡片消息状态为拒绝
    String changeMissionToRefuse(String task_id,String clicked_key);
    /**
     * 通过code 换取 企业唯一userid,请做非空判断
     * @param code
     * @return 企业唯一userid
     */
    String getUseridByCode(String code);
}
