package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.InitiationService;

import net.sf.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Controller
@RequestMapping("/itevent")
public class initiationController {
	
	@GetMapping(value = "/initiation")
    @PasssToken
    public String initiation(String adminuser,Model model,HttpServletRequest request){
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if(!"".equals(adminuser)) {
			model.addAttribute("adminuser", adminuser);
		}
		HttpSession session = request.getSession();
		String models=(String) session.getAttribute("AccessMode");
		if("pc".equals(models)) {			
			return "event/initiationpc";
		}/*else if(userAgent.contains("apple")) {  //识别apple的访问页面问题   apple的微信  企业微信     浏览器统一范文这个页面
			return "event/initiationapple";  
		}*/else {   //安卓   安卓微信    企业微信   都可以用这个也买你
			return "event/initiationmobile";
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
	@PostMapping(value="/initiation/initiationpc")
	@PasssToken
	@ResponseBody
	public JSONObject initiationpc(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("file")MultipartFile[] file, @RequestParam("userid")String userid
			, @RequestParam("phone")String phone,@RequestParam("adminuser")String adminuser,
			@RequestParam("level_1")String level_1
			, @RequestParam("level_2")String level_2, @RequestParam("description")String description
			, @RequestParam("type")String type, @RequestParam("brand")String brand
			, @RequestParam("itemid")String itemid, @RequestParam("remark")String remark){
		return initiationService.initiationpc(request,file,userid,phone,adminuser,level_1,level_2,description,type,brand,itemid,remark);
	}
	@PostMapping(value="/initiation/initiationmobile")
	@PasssToken
	@ResponseBody
	public JSONObject initiationmobile(HttpServletRequest request,HttpServletResponse response){
		//创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request是否有文件需要上传
        List<MultipartFile> file = null ;
        String userid=request.getParameter("userid");
        String phone=request.getParameter("phone");
        String adminuser=request.getParameter("adminuser");
        String level_1=request.getParameter("level_1");
        String level_2=request.getParameter("level_2");
        String description=request.getParameter("description");
        String type=request.getParameter("type");
        String brand=request.getParameter("brand");
        String itemid=request.getParameter("itemid");
        String remark=request.getParameter("remark");
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            file = multiRequest.getFiles("file");
        }
		return initiationService.initiationmobile(request,file,userid,phone,adminuser,level_1,level_2,description,type,brand,itemid,remark);
	}
}
	
