package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.InitiationService;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/itevent")
public class initiationController {
	
	@GetMapping(value = "/initiation")
    @PasssToken
    public String initiation(String adminuser,Model model){
		if(!"".equals(adminuser)) {
			model.addAttribute("adminuser", adminuser);
		}
        return "event/initiation";
    }
	
	@Autowired
	InitiationService initiationService;
	
	@GetMapping(value="/initiation/level_1")
	@PasssToken
	@ResponseBody
	public JSONObject level_1(){
		return initiationService.level_1();
	}
	@GetMapping(value="/initiation/level_2")
	@PasssToken
	@ResponseBody
	public JSONObject level_2(HttpServletRequest request,HttpServletResponse response){
		return initiationService.level_2(request);
	}
	@GetMapping(value="/initiation/description")
	@PasssToken
	@ResponseBody
	public JSONObject description(HttpServletRequest request,HttpServletResponse response){
		return initiationService.description(request);
	}
	@PostMapping(value="/initiation/postinitiation")
	@PasssToken
	@ResponseBody
	public JSONObject postinitiation(HttpServletRequest request,HttpServletResponse response){
		return initiationService.postinitiation(request);
	}
}
