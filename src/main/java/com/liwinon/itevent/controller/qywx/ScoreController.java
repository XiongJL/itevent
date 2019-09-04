package com.liwinon.itevent.controller.qywx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.ScoreService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/itevent")
public class ScoreController {
	
	@GetMapping(value = "/score")  //  事件号      qyid
    @PasssToken
    public  String access(String uuid,String qyid, String phone,String userid,String level_2,HttpServletRequest request, Model model){
        System.out.println(uuid);
        model.addAttribute("uuid",uuid);
        model.addAttribute("level_2",level_2);
        model.addAttribute("qyid",qyid);
        model.addAttribute("phone",phone);
        model.addAttribute("userid",userid);
        String userAgent = request.getHeader("user-agent").toLowerCase();
		System.out.println(userAgent);
		if(userAgent.indexOf("windows")!=-1) {			
			return "common/scorepc";
		}
        return "common/score";
    }
	
	@Autowired
	ScoreService scoreService;
	
	@GetMapping(value = "/score/save")  //  事件号      qyid
    @PasssToken
    @ResponseBody
    public  JSONObject Save(String qyid,String uuid,String score,String remark,HttpServletRequest request){
        return scoreService.Save(qyid,uuid,score,remark,request);
    }
	
}
