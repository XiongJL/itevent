package com.liwinon.itevent.controller.qywx;

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

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

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
//    @GetMapping("/receive")
//    @ResponseBody
//    @PasssToken
//    public String receiveForCheck(String msg_signature, String timestamp, String nonce, String echostr){
//        String msg = "";
//		String time = "";
//		String non = "";
//		String echo = "";
//		try {
//			msg = URLDecoder.decode(msg_signature, "utf-8");
//			time = URLDecoder.decode(timestamp, "utf-8");
//			non = URLDecoder.decode(nonce, "utf-8");
//			echo = URLDecoder.decode(echostr, "utf-8");
//			System.out.println("msg_signature:" + msg);
//			System.out.println("timestamp" + time);
//			System.out.println("nonce" + non);
//			System.out.println("echostr" + echo);
//			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(Token, EncodingAESKey, Corpid);
//			String result = wxcpt.VerifyURL(msg, time, non, echo);
//			// update用户
//			return result;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "测试中";
//    }

    /**
     * 接收APP消息
     * @return
     */
    @PostMapping("/receive")
    @ResponseBody
    public String receive(String msg_signature, String timestamp, String nonce, @RequestBody String body, HttpServletResponse response){
        WXBizMsgCrypt wxcpt = null;
		try {
			wxcpt = new WXBizMsgCrypt(WxConfig.Token.getValue(), WxConfig.EncodingAESKey.getValue(), WxConfig.Corpid.getValue());
		} catch (AesException e1) {
			e1.printStackTrace();
		}
		System.out.println("sReqMsgSig: "+msg_signature);
		System.out.println("sReqTimeStamp: "+timestamp);
		System.out.println("sReqNonce: "+nonce);
        GetMSG msg = new GetMSG(msg_signature,timestamp,nonce,body,wxcpt);
        if (WxConfig.Corpid.getValue().equals(msg.getToUserName())){
            receiveService.GetCorp(msg);
        }
        response.setStatus(200);
        return "200";
    }
}
