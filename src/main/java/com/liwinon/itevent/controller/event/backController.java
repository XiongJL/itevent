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
@RequestMapping("/itevent")
public class backController {
    @Autowired
    EventService eventService;
    @GetMapping("/back")
    @PasssToken
    public String back(){
        return "event/back";
    }

    @PostMapping("backEvent")
    @ResponseBody
    public String backEvent(int index,int event, HttpServletRequest request){

        return eventService.backEvent(index, event, request);
    }
}
