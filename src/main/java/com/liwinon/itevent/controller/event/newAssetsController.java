package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/itevent")
public class newAssetsController {
    @Autowired
    EventService eventService;
    @GetMapping(value = "/newAssets")
    @PasssToken
    public String newAssets(){
        return "event/newAssets";
    }

    //开始新资产入库事件
    @PostMapping(value = "/newAssets")
    @ResponseBody
    public String newAssetsEvent(int index, int event, HttpServletRequest request){
        return eventService.newAssetsEvent(index,event,request);
    }
}
