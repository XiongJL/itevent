package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.qywx.WxConfig;
import com.liwinon.itevent.util.GetMSG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService{
    @Autowired
    EventDao eventDao;
    @Autowired
    WxApi wxApi;
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
        String AgentID = msg.getAgentID();
        if ("1000019".equals(AgentID)){  // 处理IT服务支持的应用消息
            String MsgType =  msg.getMsgType();
            if ("event".equals(MsgType)){ //接收到的是事件
                String event =  msg.getEvent();
                String eventKey =  msg.getEventKey();
                if ("click".equals(event)&& "1".equals(eventKey)){  //用户点击了查询 , 准备发送卡片消息(展示处理进度)
                    String userid = msg.getFromUserNmae();      //查询当前用户的事件
                    List<Event> events = eventDao.findByUseridEventIng(userid);
                    String title = "您进行中的申请有"+events.size()+"件";
                    String description,btntxt,URL;
                    if (events.size()>0){
                        //可以循环赋值,展示每个事件的进度. 通过EventStep联合Event查询
                        description = ">事件a 处于xxx状态 , 处理人: xxx, 发起时间:....";
                        description = ">事件b 处于xxx状态.....";
                        URL = WxConfig.QEventURL.getValue();
                        btntxt = "查看详情";
                    }else{
                        description = "您可以点击下边的**'服务申请'**前往服务页面";
                        URL = WxConfig.ApplyURL.getValue();
                        btntxt = "服务申请";
                    }
                    return wxApi.sendCardToIT(new String[]{userid},title,description,URL,btntxt);
                }else if ("taskcard_click".equals(event)){  //是任务卡片回调信息

                }
            }
        }

        return null;
    }
}
