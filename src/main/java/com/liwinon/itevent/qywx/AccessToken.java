package com.liwinon.itevent.qywx;

import com.liwinon.itevent.util.HttpUtil;
import net.sf.json.JSONObject;

import java.util.Date;

//单例模式, 获取最后一个有效的token
public class AccessToken {
    //缓存token的Map, 保存token和时间戳
    private static class MapHandler{
        private static  volatile JSONObject info = new JSONObject();
    }
    private AccessToken() {}
    /**
     解决多线程问题方式(静态内部类)
     */
    private static final JSONObject getMemoryInfo(){
        return MapHandler.info;
    }

    public void setMap(JSONObject info){
        MapHandler.info = info;
    }

    /**
     * 对外接口 , 从缓存中获取Token或新获取Token
     * @param corpid
     * @param appsecret
     * @return
     */
    public static JSONObject getAccessToken(String corpid, String appsecret) {
        JSONObject map = getMemoryInfo();
        String time = (String) map.get("time");//从缓存中拿数据
        String accessToken = (String) map.get("access_token");//从缓存中拿数据
        Long nowDate = new Date().getTime();
        if (accessToken != null && time != null && nowDate - Long.parseLong(time) < 7100 * 1000) {
            System.out.println(new Date()+"-----从缓存读取access_token："+accessToken);
        } else {
            String param = "corpid="+corpid+"&corpsecret="+appsecret;
            String result =  HttpUtil.reqGet(WxConfig.TokenUrl.getValue(), param);
            JSONObject info = JSONObject.fromObject(result);//实际中这里要改为你自己调用微信接口去获取accessToken和jsapiticket
            //将信息放置缓存中
            map.accumulate("time", nowDate + "");
            map.accumulate("access_token", String.valueOf(info.get("access_token")));
            /**根据需求不同!还可以增加通讯的 token! 或者其他去需要缓存的数据!
             *
             * map.accumulate("member_token", String.valueOf( HttpUtil.reqGet(TokenUrl, memberParam); );
             * */

        }
        return map;
    }

}
