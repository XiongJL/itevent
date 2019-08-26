package com.liwinon.itevent.qywx;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.dao.primaryRepo.RepairUserDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.entity.primary.RepairUser;
import com.liwinon.itevent.util.HttpUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liwinon.itevent.qywx.WxConfig.*;

@Service
public class WxApiImpl implements WxApi {
    @Autowired
    EventTypeDao eventTypeDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    RepairUserDao repairUserDao;
    /**
     * 给IT事件服务APP的用户发送卡片消息
     */
    @Override
    public String sendCardToIT(String[] userid, String title, String description, String URL, String btntxt) {
        //从缓存中获取Token
        String accesstoken = AccessToken.getAccessToken(Corpid.getValue(),IThelpSecret.getValue()).getString("access_token");
        JSONObject res = card(userid,accesstoken,IThelpId.getValue(),title,description,URL,btntxt);
        System.out.println("WX返回的内容为：" + res);
        if (!"0".equals(res.getString("errcode"))) {
            return userid.toString() + "发送卡票消息失败";
        }
        return "ok";
    }

    @Override
    public String sendMissionToIT(String[] userid, String title, String description, String task_id,String[] btnKey, String[] btnName, String[] btnReplace_name,String URL) {
        String accesstoken = AccessToken.getAccessToken(Corpid.getValue(),IThelpSecret.getValue()).getString("access_token");
        JSONObject res = mission(userid,title,description,task_id,btnKey,btnName,btnReplace_name,IThelpId.getValue(),URL,accesstoken);
        if (!"0".equals(res.getString("errcode"))) {
            System.out.println(userid.toString() + "发送任务失败!!");
            return userid.toString() + "发送任务失败!!";
        }
        return "ok";
    }

    public String sendTextToOne(String[] userid,String content){
        String accesstoken = AccessToken.getAccessToken(Corpid.getValue(),IThelpSecret.getValue()).getString("access_token");
        JSONObject res =   text(userid, IThelpId.getValue(), accesstoken, content);
        if (!"0".equals(res.getString("errcode"))) {
            System.out.println(userid.toString() + "发送文本消息失败!!");
            return userid.toString() + "发送文本消息失败!!";
        }
        return "ok";
    }

    @Override
    //更改任务状态为拒绝
    public String changeMissionToRefuse(String task_id, String clicked_key) {
        Event e= eventDao.findByUuid(task_id);
        String team  =  eventTypeDao.findByETypeId(e.getEvent()).getTeam();
        //获取所有team成员
        List<RepairUser> teams =  repairUserDao.findByTeam(team);
        String[] users = new String[teams.size()];;
        for (int i= 0;i<teams.size();i++){
            users[i] = teams.get(i).getUserid(); //获取企业微信账号
        }
        String accesstoken = AccessToken.getAccessToken(Corpid.getValue(),IThelpSecret.getValue()).getString("access_token");
        JSONObject res = changeMissionState(users,IThelpId.getValue(),accesstoken,clicked_key,task_id);
        if (!"0".equals(res.getString("errcode"))) {
            System.out.println(task_id + "更改任务状态为拒绝失败!!");
            return task_id + "更改任务状态为拒绝失败!!";
        }
        return "ok";
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

    /**
     * 发送任务消息实现
     * 详细接口说明在
     *  https://work.weixin.qq.com/api/doc#90000/90135/90236/%E4%BB%BB%E5%8A%A1%E5%8D%A1%E7%89%87%E6%B6%88%E6%81%AF
     * @param userid
     * @param title
     * @param description
     * @param task_id
     *     "btn":[
     *                 {
     *                     "key": "key111",
     *                     "name": "批准",
     *                     "replace_name": "已批准",
     *                     "color":"red",
     *                     "is_bold": true
     *                 },
     *                 {
     *                     "key": "key222",
     *                     "name": "驳回",
     *                     "replace_name": "已驳回"
     *                 }
     *             ]
     * @param agentid
     * @param URL
     * @return
     */
    static JSONObject mission(String[] userid,String title, String description,String task_id,
                              String[] btnKey, String[] btnName, String[] btnReplace_name,
                              String agentid,String URL,String token){
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
        JSONObject json = new JSONObject();
        JSONObject taskcard = new JSONObject();
        JSONArray btn = new JSONArray();
        /*构建按钮组*/
        for (int i = 0 ; i<btnKey.length;i++){
            JSONObject temp = new JSONObject();
            temp.accumulate("key",btnKey[i]);
            temp.accumulate("name",btnName[i]);
            temp.accumulate("replace_name",btnReplace_name[i]);
            btn.add(temp);
        }
        /*构建完成*/
        json.accumulate("touser",users);
        json.accumulate("msgtype", "taskcard");
        json.accumulate("agentid", agentid);
        taskcard.accumulate("title", title);
        taskcard.accumulate("description", description);
        taskcard.accumulate("url", URL);
        taskcard.accumulate("task_id", task_id);
        taskcard.accumulate("btn", btn);
        json.accumulate("taskcard",taskcard);
        String param = json.toString();
        System.out.println("json:" + param);
        JSONObject res = JSONObject.fromObject(HttpUtil.reqPost(
                "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, param));
        System.out.println("WX返回的：" + res);
        return res;
    }

    /**
     * 更新任务卡片消息状态实现
     * @return
     */
    static JSONObject changeMissionState(String[] userid,String agentid,String token,String clicked_key,
                                         String task_id){
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
        JSONObject json = new JSONObject();
        json.accumulate("userids",users);
        json.accumulate("agentid",agentid);
        json.accumulate("task_id",task_id);
        json.accumulate("clicked_key",clicked_key);
        String param = json.toString();
        System.out.println("json:" + param);
        JSONObject res = JSONObject.fromObject(HttpUtil.reqPost(
                "https://qyapi.weixin.qq.com/cgi-bin/message/update_taskcard?access_token=" + token, param));
        System.out.println("WX返回的：" + res);
        return res;
    }
    /**
     * 发送文本消息实现
     * @param userid
     * @param agentid
     * @param token
     * @param content
     * @return
     */
    static JSONObject text(String[] userid,String agentid,String token,String content){
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
        JSONObject json = new JSONObject();
        JSONObject str = new JSONObject();
        str.accumulate("content",content);
        json.accumulate("touser",users);
        json.accumulate("msgtype","text");
        json.accumulate("agentid",agentid);
        json.accumulate("text",str);
        json.accumulate("safe",0);
        String param = json.toString();
        System.out.println("json:" + param);
        JSONObject res = JSONObject.fromObject(HttpUtil.reqPost(
                "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token,param));

        return res;
    }



}
