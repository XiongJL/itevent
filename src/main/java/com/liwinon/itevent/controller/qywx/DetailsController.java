package com.liwinon.itevent.controller.qywx;

import static com.liwinon.itevent.exception.ResultEnum.ERROR_6;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.exception.MyException;
import com.liwinon.itevent.qywx.WxConfig;
import com.liwinon.itevent.service.DetailsService;


@Controller
@RequestMapping(value = "/itevent")
public class DetailsController {
	
	@Autowired
	DetailsService detailsServive;
	
	@GetMapping(value = "/details")  // 事件号  ，qyid    工号
	@PasssToken
	public  String Details(String qywxid,HttpServletRequest request, Model model){
		Map<String,Object>  map=detailsServive.queryDetails(qywxid);
		if (map==null){
			throw new MyException(ERROR_6.getCode(), ERROR_6.getMsg());
		}
		model.addAttribute("qywxid",qywxid);
		model.addAttribute("personid",map.get("personid"));
		model.addAttribute("data",map.get("data"));
		String userAgent = request.getHeader("user-agent").toLowerCase();
		System.out.println(userAgent);
		if(userAgent.indexOf("windows")!=-1) {			
			return "common/detailspc";
		}
		return "common/details";
	}
	
	@GetMapping(value = "/urlqmission")
    @PasssToken
    public String urlqmission(String qyid, String uuid, HttpServletRequest request,HttpServletResponse res){
		System.out.println(qyid+"----------"+uuid);
	        try {
	           res.sendRedirect("https://mesqrcode.liwinon.com/itevent/qMission?uuid="+uuid+"&qyid="+qyid);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return null;
    }
}
