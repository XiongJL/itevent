package com.liwinon.itevent.controller;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.IndexService;
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
    @GetMapping("/itevent/login")
    @PasssToken
    public String login(){

        return "login";
    }
    @GetMapping("/itevent/index")
    @PasssToken
    public String index(HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (StringUtils.isEmpty(username)){
            return "login";
        }
        return "index";
    }
    @PostMapping("/itevent/tologin")
    @ResponseBody
    @PasssToken
    public String tologin(String username, String pwd, HttpServletRequest request){
        return index.login(username,pwd,request);
    }
}
