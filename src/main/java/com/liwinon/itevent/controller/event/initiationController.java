package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.InitiationService;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/itevent")
public class initiationController {
	
	@GetMapping(value = "/initiation")
    @PasssToken
    public String initiation(String adminuser,Model model,HttpServletRequest request){
		if(!"".equals(adminuser)) {
			model.addAttribute("adminuser", adminuser);
		}
		
		HttpSession session = request.getSession();
		String models=(String) session.getAttribute("AccessMode");
		if("pc".equals(models)) {			
			System.out.println("pc端=================");
			return "event/initiationpc";
		}else {
			System.out.println("移动端--------------------");
			return "event/initiation";
		}
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
	public JSONObject postinitiation(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("file")MultipartFile[] files, @RequestParam("userid")String userid
			, @RequestParam("phone")String phone,@RequestParam("adminuser")String adminuser,
			@RequestParam("level_1")String level_1
			, @RequestParam("level_2")String level_2, @RequestParam("description")String description
			, @RequestParam("type")String type, @RequestParam("brand")String brand
			, @RequestParam("itemid")String itemid, @RequestParam("remark")String remark){
		return initiationService.postinitiation(request,files,userid,phone,adminuser,level_1,level_2,description,type,brand,itemid,remark);
	}
}
