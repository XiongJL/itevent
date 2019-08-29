package com.liwinon.itevent.controller.qywx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liwinon.itevent.annotation.PasssToken;

@Controller
@RequestMapping(value = "/itevent")
public class AccessController {
	
	 @GetMapping(value = "/access")  //  事件号      qyid
	    @PasssToken
	    public  String access(String uuid,String qyid, HttpServletRequest request, Model model){
	        System.out.println(uuid);
	        model.addAttribute("uuid",uuid);
	        model.addAttribute("qyid",qyid);
	        String userAgent = request.getHeader("user-agent").toLowerCase();
			System.out.println(userAgent);
			if(userAgent.indexOf("windows")!=-1) {			
				return "common/access";
			}
	        return "common/access";
	    }
}
