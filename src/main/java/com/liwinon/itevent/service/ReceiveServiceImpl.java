package com.liwinon.itevent.service;

import com.liwinon.itevent.util.GetMSG;
import org.springframework.stereotype.Service;

@Service
public class ReceiveServiceImpl implements ReceiveService{

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
         * <xml>
         * <ToUserName><![CDATA[wwbc7acf1bd2c6f766]]></ToUserName>
         * <FromUserName><![CDATA[1902268014]]></FromUserName>
         * <CreateTime>1566174435</CreateTime>
         * <MsgType><![CDATA[event]]></MsgType>
         * <AgentID>1000019</AgentID>
         * <Event><![CDATA[click]]></Event>
         * <EventKey><![CDATA[1]]></EventKey>
         * </xml>
         */
        String AgentID = msg.getAgentID();
        if ("1000019".equals(AgentID)){  // 处理IT服务支持的应用消息
            String MsgType =  msg.getMsgType();
            if ("event".equals(MsgType)){ //接收到的是事件
                String Event =  msg.getEvent();
                String EventKey =  msg.getEventKey();
                if ("click".equals(Event)&& "1".equals(EventKey)){  //用户点击了查询 , 准备发送卡片消息(展示处理进度)
                    //查询当前用户的事件

                }
            }
        }

        return null;
    }
}
