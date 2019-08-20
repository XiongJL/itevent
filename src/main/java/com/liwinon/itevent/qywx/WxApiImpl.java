package com.liwinon.itevent.qywx;

import com.liwinon.itevent.util.HttpUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WxApiImpl implements WxApi {

    /**
     * 给IT事件服务APP的用户发送卡片消息
     */
    @Override
    public String sendCardToIT(String[] userid, String title, String description, String URL, String btntxt) {
        //从缓存中获取Token
        String accesstoken = AccessToken.getAccessToken(WxConfig.Corpid.getValue(),WxConfig.IThelpSecret.getValue()).getString("access_token");
        JSONObject res = card(userid,accesstoken,WxConfig.IThelpId.getValue(),title,description,URL,btntxt);
        System.out.println("WX返回的内容为：" + res);
        if (!"0".equals(res.getString("errcode"))) {
            return userid.toString() + "发送卡票消息失败";
        }
        return null;
    }

    /**
     *
     * @param userid    String[]  准备发送的用户
     * @param token     accessToken
     * @param agentid   应用ID
     * @param title 卡片标题
     * @param description   卡片描述
     * @param URL   点击打开的网页
     * @param btntxt    按钮的文本
     * @return  null,发送人为空, 正常为JSONObject
     */
    //发送卡片消息实现.
    static JSONObject card(String[] userid, String token, String agentid, String title, String description, String URL, String btntxt){
        String users = "";
        if (userid.length>1){
            for (String user : userid){
                users += user +"|";
            }
            users = users.substring(0,users.length()-1);
        }else if (userid.length==1){
            users = userid[0];
        }else{
            return null;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> secondtMap = new HashMap<String, Object>(); // json
        resultMap.put("touser", users);
        resultMap.put("msgtype", "textcard");
        resultMap.put("agentid", agentid);
        secondtMap.put("title", title);
        secondtMap.put("description", description);
        secondtMap.put("url", URL);
        secondtMap.put("btntxt", btntxt);
        resultMap.put("textcard", secondtMap);
        JSONObject json = JSONObject.fromObject(resultMap);
        String param = json.toString();
        System.out.println("json:" + param);
        JSONObject res = JSONObject.fromObject(HttpUtil.reqPost(
                "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, param));
        System.out.println("WX返回的：" + res);
        return res;
    }
}
