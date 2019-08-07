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
public class oldAssetsController {

    @Autowired
    EventService eventService;
    @GetMapping(value = "/oldAssets")
    @PasssToken
    public String oldAssets(){
        return "event/oldAssets";
    }

    @PostMapping(value = "/oldAssets")
    @ResponseBody
    public String oldAssetsEvent(int index, int event, HttpServletRequest request){
        return eventService.oldAssetsEvent(index,event,request);
    }
}
