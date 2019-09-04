package com.liwinon.itevent.controller.qywx;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.RepairUser;
import com.liwinon.itevent.exception.MyException;
import com.liwinon.itevent.service.MissionService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.liwinon.itevent.exception.ResultEnum.ERROR_6;

@Controller
@RequestMapping(value = "/itevent")
public class MissionController {
    @Autowired
    MissionService mission;

    @GetMapping(value = "/qMission")  //
    @PasssToken
    public  String qMission(String uuid,String qyid, HttpServletRequest request, Model model){
        System.out.println(uuid);
        Map<String,Object> res = mission.queryMission(uuid);
        if (res==null){
            throw new MyException(ERROR_6.getCode(), ERROR_6.getMsg());
        }
        model.addAttribute("start",res.get("start"));
        model.addAttribute("ing",res.get("ing"));
        model.addAttribute("imgUrls",res.get("imgUrls"));
        model.addAttribute("imgLength",res.get("imgLength"));
        model.addAttribute("qyid",qyid);
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String AccessMode = (String)session.getAttribute("AccessMode");
        if ("pc".equals(AccessMode)){
            return "common/qMission-PC";
        }
        return "common/qMission";
    }

    /**
     * 查询在当前时间段内可以转交的人员
     * @return
     */
    @GetMapping(value = "/mission/CanTurnOverUsers")
    @ResponseBody
    public JSONObject CanTurnOverUsers(String uuid, String qyid, HttpServletRequest request){

        return mission.CanTurnOverUsers(uuid,qyid,request);
    }


    /**
     * 页面选择移交处理
     */
    @GetMapping(value = "/mission/changeUser")
    @ResponseBody
    public JSONObject changeUser(String toPersonid, String fromPersonid, String uuid,String qyid,HttpServletRequest request){
        return mission.changeUser(toPersonid,fromPersonid,uuid,qyid,request);
    }
    
    
    /**   yanxy
     * 页面选择完成处理
     */
    @GetMapping(value = "/mission/complete")
    @ResponseBody
    public JSONObject complete(String fromPersonid, String uuid,String qyid,String remark,HttpServletRequest request){
    	return mission.complete(fromPersonid,uuid,qyid,remark,request);
    }

    /**
     * 查询用户的进行中事件
     * @return
     */
    @GetMapping(value = "/qEvent")
    @PasssToken
    public String qEvent(String qyid,Model model){
       Map<String,Object> res =  mission.queryEvent(qyid);
       model.addAttribute("count",res.get("count"));
       model.addAttribute("data",res.get("data"));
       return "common/qEvent";
    }

}
