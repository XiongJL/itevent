package com.liwinon.itevent.controller;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.ApiService;
import com.liwinon.itevent.service.IndexService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class indexController {
    @Autowired
    IndexService index;
    @Autowired
    ApiService api;
    @GetMapping("/itevent/login")
    @PasssToken
    public String login(HttpServletRequest request){
        String userAgent = request.getHeader("user-agent").toLowerCase();
        System.out.println(userAgent);
        String AccessMode = "";
        if(userAgent.indexOf("micromessenger")!= -1){
            //微信
            AccessMode = "wx";
        }else if(userAgent.indexOf("wxwork") != -1){
            //企业微信
            AccessMode = "qywx";
        }
        else if(userAgent.indexOf("android") != -1){
            //安卓
            AccessMode = "android";
        }else if(userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1 || userAgent.indexOf("ipod") != -1){
            //苹果
            AccessMode = "apple";
        }else{
            //电脑
            AccessMode = "pc";
        }
        HttpSession session = request.getSession();
        session.setAttribute("AccessMode",AccessMode);
        return "login";
    }
    @GetMapping("/itevent/index")
    @PasssToken
    public String index(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (StringUtils.isEmpty(username)){
            return "login";
        }
        JSONObject json = api.getAssetsNums();
        model.addAttribute("count",json);
        return "index";
    }
    @PostMapping("/itevent/tologin")
    @ResponseBody
    @PasssToken
    public String tologin(String username, String pwd, HttpServletRequest request){
        return index.login(username,pwd,request);
    }

    @GetMapping("/itevent/logout")
    @PasssToken
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "login";
    }

    /**
     * 返回用户登录前想访问的页面
     */
    @GetMapping("/itevent/originURL")
    @PasssToken
    @ResponseBody
    public String originURL(HttpServletRequest request){
        HttpSession session = request.getSession();
        String URL = (String) session.getAttribute("OriginUrl");
        session.setAttribute("OriginUrl",null);
        return URL;
    }
}
