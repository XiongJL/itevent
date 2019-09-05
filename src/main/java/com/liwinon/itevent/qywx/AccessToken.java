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
        System.out.println(new Date().toString()+"---准备获取token");
        JSONObject map = getMemoryInfo();
        String time = (String) map.get("time");//从缓存中拿数据
        String accessToken = (String) map.get("access_token");//从缓存中拿数据
        Long nowDate = new Date().getTime();
        if (accessToken != null && time != null && nowDate - Long.parseLong(time) <= 7200 * 1000) {
            if (nowDate - Long.parseLong(time) < 3000 * 1000&&nowDate - Long.parseLong(time) > 2800 * 1000){  //3000 S-2500S 就验证token 的有效性
                JSONObject json = JSONObject.fromObject(HttpUtil.reqGet("https://qyapi.weixin.qq.com/cgi-bin/agent/get",
                        "access_token=" + map.getString("access_token") + "&agentid=" + WxConfig.IThelpId));
                if (!"0".equals(json.getString("errcode"))) { // 如果调用失败
                    map = postWx(corpid,appsecret,nowDate);
                }
            }
            System.out.println(new Date()+"-----从缓存读取access_token："+accessToken);
        } else {
            map = postWx(corpid,appsecret,nowDate);
        }
        return map;
    }


    private static JSONObject postWx(String corpid, String appsecret,Long nowDate){
        JSONObject map = new JSONObject();
        String param = "corpid="+corpid+"&corpsecret="+appsecret;
        String result =  HttpUtil.reqGet(WxConfig.TokenUrl.getValue(), param);
        System.out.println("----从网络获取到的token返回数据 : " +result);
        try {
            JSONObject info = JSONObject.fromObject(result);//实际中这里要改为你自己调用微信接口去获取accessToken和jsapiticket
            if ("0".equals(info.getString("errcode"))){
                //将信息放置缓存中
                // accumulate()  累积value到这个key下,如果当前已经存在一个value在这个key下那么一个JSONArray将会存储在这个key下来保存所有累积的value。
                //put会覆盖,已存在的值!!! ,这里用put
                map.put("time", nowDate + "");
                map.put("access_token", String.valueOf(info.get("access_token")));
            }else{
                System.out.println("获取token失败!!!!!!");
            }

            /**根据需求不同!还可以增加通讯的 token! 或者其他需要缓存的数据!
             *
             * map.put("member_token", String.valueOf( HttpUtil.reqGet(TokenUrl, memberParam); );
             * */

        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

}
