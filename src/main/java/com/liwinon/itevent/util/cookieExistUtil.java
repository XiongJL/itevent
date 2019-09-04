package com.liwinon.itevent.util;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liwinon.itevent.qywx.WxConfig;


public class cookieExistUtil {
	
	 public static String cookieExist(HttpServletRequest request,HttpServletResponse response) {
	        String userid =null;
	        Cookie cookies[] = request.getCookies();
	        if(cookies==null || cookies.length == 0) {
	            System.out.println("没有cookie");
	            //不存在调用Service获取userid
	            getCode(request,response);
	        }else{
	            //存在则继续操作
	            boolean exist = false;
	            for (Cookie cookie : cookies) {
	                System.out.println("---------------");
	                //获取cookie的解释内容
	                //获取cookie的键
	                String key = cookie.getName();
	                System.out.println("key:"+key);
	                //获取cookie的值
	                String value = cookie.getValue();
	                System.out.println("value:"+value);
	                //获取cookie的有效时间。
	                int time = cookie.getMaxAge();
	                System.out.println("time:"+time);
	                if("userid".equals(key)){  //如果存在userid,正常访问
	                    System.out.println("有useridCookie!!!!");
	                    userid = value;
	                    exist = true;
	                }
	            }
	            if(!exist){
	                //循环完毕都不存在userid ,重定向获取值
	                System.out.println("不存在cookie!!!!");
	                getCode(request,response);
	            }

	        }
	        return userid;
	    }
	 
	 public static void getCode(HttpServletRequest request, HttpServletResponse response) {
		        String corpid ="wwbc7acf1bd2c6f766";
		        String redirect_uri =WxConfig.ApplyURL.getValue();
		        System.out.println("转码后的uri为:"+redirect_uri);
		        String URL = WxConfig.Oauth2URL.getValue() +
		                "appid="+corpid+
		                "&redirect_uri="+redirect_uri+
		                "&response_type=code"+
		                "&scope=snsapi_base"+
		                "&state=STATE#wechat_redirect";  //state不是必须,重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
		        //员工点击后，页面将跳转至 redirect_uri?code=CODE&state=STATE    code用以换取userid
		        try {
		            response.sendRedirect(URL); //重定向
		        } catch (IOException e) {
		            System.out.println("重定向到OAutho2.0失败!");
		            e.printStackTrace();
		        } catch (IllegalStateException e){
		            System.out.println("捕获到 sendRedirect异常!");
		            //重定向到 err?  ,错误页面直接跳转到主页?  cookie 永久保存(最后做.)?
		        }
		    }
	 public static String getcookie(HttpServletRequest request,HttpServletResponse response) {
	        String userid =null;
	        Cookie cookies[] = request.getCookies();
            //存在则继续操作
            for (Cookie cookie : cookies) {
                //获取cookie的解释内容
                //获取cookie的键
                String key = cookie.getName();
                //获取cookie的值
                String value = cookie.getValue();
                //获取cookie的有效时间。
                int time = cookie.getMaxAge();
                if("userid".equals(key)){  //如果存在userid,正常访问
                    userid = value;
                }
            }
	        return userid;
	    }
}
