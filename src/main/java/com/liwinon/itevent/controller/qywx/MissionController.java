package com.liwinon.itevent.controller.qywx;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.exception.MyException;
import com.liwinon.itevent.service.MissionService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.liwinon.itevent.exception.ResultEnum.ERROR_6;

@Controller
@RequestMapping(value = "/itevent")
public class MissionController {
    @Autowired
    MissionService mission;

    @GetMapping(value = "/qMission")  //
    @PasssToken
    public  String qMission(String uuid, HttpServletRequest request, Model model){
        System.out.println(uuid);
        Map<String,Object> res = mission.queryMission(uuid);
        if (res==null){
            throw new MyException(ERROR_6.getCode(), ERROR_6.getMsg());
        }
        model.addAttribute("start",res.get("start"));
        model.addAttribute("ing",res.get("ing"));
        model.addAttribute("imgUrls",res.get("imgUrls"));
        model.addAttribute("imgLength",res.get("imgLength"));
        HttpSession session = request.getSession();
        String AccessMode = (String)session.getAttribute("AccessMode");
        if ("pc".equals(AccessMode)){
            return "common/qMission-PC";
        }
        return "common/qMission";
    }

}
