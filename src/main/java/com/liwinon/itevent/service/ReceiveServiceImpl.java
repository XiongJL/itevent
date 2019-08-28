package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.*;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.entity.primary.RepairUser;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.qywx.WxConfig;
import com.liwinon.itevent.service.ReceiveService;
import com.liwinon.itevent.util.GetMSG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService {
    @Autowired
    EventDao eventDao;
    @Autowired
    EventStepDao eventStepDao;
    @Autowired
    EventTypeDao eventTypeDao;
    @Autowired
    SapDao sapDao;
    @Autowired
    WxApi wxApi;
    @Autowired
    RepairUserDao repairUserDao;
    /**
     * 接收并处理企业微信发送的消息/事件
     * @param msg
     * @return
     */
    @Override
    public String GetCorp(GetMSG msg) {
        //接收企业APP - IT服务支持APP - 发送的消息
        /*
         * after decrypt msg:
         *  接收按钮事件
         * <xml>
         * <ToUserName><![CDATA[wwbc7acf1bd2c6f766]]></ToUserName>
         * <FromUserName><![CDATA[1902268014]]></FromUserName>
         * <CreateTime>1566174435</CreateTime>
         * <MsgType><![CDATA[event]]></MsgType>
         * <AgentID>1000019</AgentID>
         * <Event><![CDATA[click]]></Event>
         * <EventKey><![CDATA[1]]></EventKey>
         * </xml>
         *
         *   任务消息回调
         * <xml>
            <ToUserName><![CDATA[toUser]]></ToUserName>
            <FromUserName><![CDATA[FromUser]]></FromUserName>
            <CreateTime>123456789</CreateTime>
            <MsgType><![CDATA[event]]></MsgType>
            <Event><![CDATA[taskcard_click]]></Event>
            <EventKey><![CDATA[key111]]></EventKey>
            <TaskId><![CDATA[taskid111]]></TaskId >
            <AgentID>1</AgentID>
            </xml>
         */
        System.out.println();
       try {
           String AgentID = msg.getAgentId();
           if ("1000019".equals(AgentID)){  // 处理IT服务支持的应用消息
               String MsgType =  msg.getMsgType();
               System.out.println("接收到的消息类型:"+MsgType);
               if ("event".equals(MsgType)){ //接收到的是事件
                   String event =  msg.getEvent();
                   String eventKey =  msg.getEventKey();
                   System.out.println("接收到的event:"+event);
                   System.out.println("接收到的eventKey:"+eventKey);
                   if ("click".equals(event)&& "1".equals(eventKey)){  //用户点击了查询 , 准备发送卡片消息(展示处理进度)
                       System.out.println("接收到用户点击的查询事件");
                       String userid = msg.getFromUserName();      //查询当前用户的事件
                       //查询该企业微信对应的工号 ,  普通用户只找qyid
                       List<Event> events = new ArrayList<>();
                       events = eventDao.findByUseridEventIng(userid); //用工号找 , 通常企业微信号等同于工号
                       if (events.size()<=0){
                           events = eventDao.findByQyidEventIng(userid);  //用企业微信查
                       }
                       String title = "您进行中的申请有"+events.size()+"件";
                       String description,btntxt,URL;
                       if (events.size()>0){
                           //可以循环赋值,展示每个事件的进度. 通过EventStep联合Event查询
                           int times = 0;description="";
                           for (Event tmp : events){
                               if (times>4){
                                   description += "<br>...";
                                   break;
                               }
                               String type =  eventTypeDao.findByETypeId(tmp.getEvent()).getLevel_1();
                               description += "您的"+type+"相关的申请,处于: "+tmp.getState()+"<br>";
                               times++;
                           }
                           URL = WxConfig.QEventURL.getValue()+"?qyid="+userid;
                           btntxt = "查看详情";
                       }else{
                           description = "您可以点击下边的'服务申请'前往申请页面";
                           URL = WxConfig.ApplyURL.getValue();
                           btntxt = "服务申请";
                       }
                       return wxApi.sendCardToIT(new String[]{userid},title,description,URL,btntxt);
                   }else if ("taskcard_click".equals(event)){  //是任务卡片回调信息
                       System.out.println("接收到任务卡片回调");
                       //获取卡片的task_id , 等同于事件的uuid.
                       String var = msg.getTaskId();
                       String[] str =  var.split("-");
                       String task_id = "";
                       for (int i= 0;i<str.length-1;i++){ //拼接最后一个流水号, 防止发送的uuid不匹配.
                           task_id +=str[i] +"-";
                       }
                       task_id = task_id  +  "1";
                       System.out.println(task_id);
                       SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
                       Date date = new Date();
                       String dateStr =  sdf.format(date);
                       EventStep step = eventStepDao.findByUuidAndStep1(task_id);
                       Event e = eventDao.findByUuid(task_id);
                       String executorId = step.getExecutorId();
                       String executorName = step.getExecutorName();
                       String EventKey = msg.getEventKey();
                       if ("2".equals(EventKey)){ //如果本次回调是按得接受按钮
                           //尚未有任务执行人
                           if (StringUtils.isEmpty(executorId)){
                               //添加该人为执行人
                               String FromUserName = msg.getFromUserName();
                               //从维修人员表通过微信号获取人员工号姓名
                               RepairUser repairUser =  repairUserDao.findByUserid(FromUserName);
                               String id = repairUser.getPersonid() ;
                               String name = repairUser.getName();

                               step.setExecutorId(id);
                               step.setExecutorName(name);
                               EventStep next = new EventStep();
                               next.setUuid(step.getUuid());
                               next.setStep(2);
                               next.setExecutorName(name);
                               next.setExecutorId(id);
                               next.setImgurl(step.getImgurl());
                               next.setStepDate(date);
                               e.setState("处理中");
                               eventDao.save(e);
                               eventStepDao.save(step);
                               eventStepDao.save(next);
                               //回执卡片信息, 方便执行人查看详细数据,以及更新环节
                               EventType eType = eventTypeDao.findByETypeId(e.getEvent());
                               String title = "您已接受"+sapDao.findNByUserId(e.getUserid())+":"+e.getPhone()+"提出的申请";
                               String description = dateStr+"<br>"+"事件类型:"+eType.getLevel_2()+"<br>"+"用户描述:"+e.getRemark();
                               String btntxt = "查看详情";
                               String URL = WxConfig.QMissionURL.getValue()+task_id+"&qyid="+FromUserName;
                               return wxApi.sendCardToIT(new String[]{FromUserName},title,description,URL,btntxt);

                           }else{
                               //已经被人接收. 回执
                               System.out.println(task_id+" : 已被其他人接收");
                               return wxApi.sendTextToOne(new String[]{msg.getFromUserName()},"该申请已由"+executorName+"处理");
                           }
                       }else if ("1".equals(EventKey)){  //拒绝
                           //尚未有任务执行人
                           if (StringUtils.isEmpty(executorId)){
                               //发送文本消息给该人
                               e.setState("已拒绝");
                               eventDao.save(e);
                               //请求更新任务卡片的信息
                               String res = wxApi.changeMissionToRefuse(task_id,"1");
                               if (res!="ok"){
                                   System.out.println("更新任务信息为拒接失败! : task_id:"+task_id);
                               }
                               return wxApi.sendTextToOne(new String[]{msg.getFromUserName()},"您的申请已被拒绝,请尝试更改内容后重新申请.");

                           }else{  //已有执行人, 无法拒绝
                               //发送卡片信息,告知拒绝者已有人处理.
                               return wxApi.sendCardToIT(new String[]{msg.getFromUserName()},"拒绝操作失败提示!"
                                       ,"当前服务申请已有人接收受处理!",
                                       WxConfig.QMissionURL.getValue()+task_id+"&qyid="+msg.getFromUserName(),"了解详情");
                           }
                       }else{
                           System.out.println("按钮值不对应!值为:"+EventKey);
                       }

                   }
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }
}
