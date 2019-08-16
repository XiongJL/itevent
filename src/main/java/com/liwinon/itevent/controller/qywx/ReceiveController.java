package com.liwinon.itevent.controller.qywx;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.util.AesException;
import com.liwinon.itevent.util.WXBizMsgCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLDecoder;

@Controller
@RequestMapping("/itevent")
public class ReceiveController {
    private static final String Token = "j2guDGx";
    private static final String EncodingAESKey = "f3DERzDMO2ys9X4W0F7YNDkN353F93toR86UGDKoK7u";
    private static final String Corpid = "wwbc7acf1bd2c6f766";


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
    public String receive(String msg_signature,String timestamp,String nonce,@RequestBody String body){
        WXBizMsgCrypt wxcpt = null;
		try {
			wxcpt = new WXBizMsgCrypt(Token, EncodingAESKey, Corpid);
		} catch (AesException e1) {
			e1.printStackTrace();
		}
		 System.out.println("sReqMsgSig: "+msg_signature);
		 System.out.println("sReqTimeStamp: "+timestamp);
		 System.out.println("sReqNonce: "+nonce);
		 String sMsg;
			try {
				sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, body);
				System.out.println("after decrypt msg: " + sMsg);
				// TODO: 解析出明文xml标签的内容进行处理
				// For example:
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				StringReader sr = new StringReader(sMsg);
				InputSource is = new InputSource(sr);
				Document document = db.parse(is);
				Element root = document.getDocumentElement();
				String userName =  root.getElementsByTagName("ToUserName").item(0).getTextContent();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return "ok";
    }
}
