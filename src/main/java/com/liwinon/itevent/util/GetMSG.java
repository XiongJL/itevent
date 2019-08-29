package com.liwinon.itevent.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 *   工具类,  解析发送的密文  ---> XML ,并读取XML
 *   获取用户发送的消息, 返回发送者id, 消息接收者id, 发送内容, 内容类型 ,发送时间(时间戳)
 * @author 1902268014
 *
 */
public class GetMSG {
	private Element Root;
	
	// 构造函数
	public GetMSG(String sReqMsgSig, String sReqTimeStamp, String sReqNonce, String sReqData, WXBizMsgCrypt wxcpt) {
		super();
		//初始化消息的文本
		String sMsg;
		try {
			sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
			System.out.println("after decrypt msg: " + sMsg);
			// TODO: 解析出明文xml标签的内容进行处理
			// For example:
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(sMsg);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			this.Root = root;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**获取消息接收者id*/
	public String getToUserName() {
		return Root.getElementsByTagName("ToUserName").item(0).getTextContent(); 
	}
	/**获取消息发送者id*/
	public String getFromUserName() {
		return Root.getElementsByTagName("FromUserName").item(0).getTextContent(); 
	}
	/**获取消息创建时间戳*/
	public String getCreateTime() {
		return Root.getElementsByTagName("CreateTime").item(0).getTextContent(); 
	}
	/**获取消息类型*/
	public String getMsgType() {
		return Root.getElementsByTagName("MsgType").item(0).getTextContent(); 
	}
	/**获取消息内容*/
	public String getContent() {
		return Root.getElementsByTagName("Content").item(0).getTextContent(); 
	}
	/**获取消息id*/
	public String getMsgId() {
		return Root.getElementsByTagName("MsgId").item(0).getTextContent(); 
	}
	/**获取应用的id*/
	public String getAgentId() {
		/*傻逼腾讯传两种ID ,Id**/
		if (Root.getElementsByTagName("AgentId").item(0) == null){
			return Root.getElementsByTagName("AgentID").item(0).getTextContent();
		}else{
			return Root.getElementsByTagName("AgentId").item(0).getTextContent();
		}
	}
	/**获取事件类型*/
	public String getEvent() {
		return Root.getElementsByTagName("Event").item(0).getTextContent();
	}
	/**获取事件号,当是卡片任务时获取的为btn:key按钮值*/
	public String getEventKey() {
		return Root.getElementsByTagName("EventKey").item(0).getTextContent();
	}
	/**获取卡片任务id*/
	public String getTaskId() {
		return Root.getElementsByTagName("TaskId").item(0).getTextContent();
	}
}
