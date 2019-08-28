package com.liwinon.itevent.controller.qywx;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.qywx.WxConfig;
import com.liwinon.itevent.service.ReceiveService;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.util.AesException;
import com.liwinon.itevent.util.GetMSG;
import com.liwinon.itevent.util.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLDecoder;


@Controller
@RequestMapping("/itevent")
public class ReceiveController {
    //接收消息接口token和秘钥

    @Autowired
    ReceiveService receiveService;

    /**
     * 验证接口
     * @return
     */
    @GetMapping("/receive")
    @ResponseBody
    @PasssToken
    public String receiveForCheck(String msg_signature, String timestamp, String nonce, String echostr){
        String msg = "";
		String time = "";
		String non = "";
		String echo = "";
		System.out.println("echostr原来的:"+echostr);
		try {
			msg = URLDecoder.decode(msg_signature, "utf-8");
			time = URLDecoder.decode(timestamp, "utf-8");
			non = URLDecoder.decode(nonce, "utf-8");
			echo = URLDecoder.decode(echostr, "utf-8");
			System.out.println("msg_signature:" + msg);
			System.out.println("timestamp" + time);
			System.out.println("nonce" + non);
			//解码后 + 号变空格, 关键接收的时候自动解码了???
			System.out.println("echostr解码后的:" + echostr);
			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WxConfig.Token.getValue(), WxConfig.EncodingAESKey.getValue(), WxConfig.Corpid.getValue());
			String result = wxcpt.VerifyURL(msg, time, non, echostr);
			System.out.println("结果是:"+result);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "测试中";
    }

    /**
     * 接收APP消息
     * @return
     */
    @PostMapping("/receive")
    @ResponseBody
	@PasssToken
    public String receive(String msg_signature,String timestamp,String nonce,@RequestBody String body){
        WXBizMsgCrypt wxcpt = null;
		try {
			wxcpt = new WXBizMsgCrypt(WxConfig.Token.getValue(), WxConfig.EncodingAESKey.getValue(), WxConfig.Corpid.getValue());
			System.out.println("sReqMsgSig: "+msg_signature);
			System.out.println("sReqTimeStamp: "+timestamp);
			System.out.println("sReqNonce: "+nonce);
			System.out.println("body: "+body);
			GetMSG msg = new GetMSG(msg_signature,timestamp,nonce,body,wxcpt);

			if (WxConfig.Corpid.getValue().equals(msg.getToUserName())){
				receiveService.GetCorp(msg);
			}
			return "200";
		} catch (AesException e1) {
			e1.printStackTrace();
		}
		//无论如何返回成功
		return "200";
    }
}
