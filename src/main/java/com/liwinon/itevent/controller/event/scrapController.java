package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/itevent")
public class scrapController {
    @Autowired
    EventService eventService;
    @GetMapping("/scrap")
    @PasssToken
    public String scrap(){
        return "event/scrap";
    }
    @PostMapping("/scrapEvent")
    @ResponseBody
    public String scrapEvent(int index, int event, HttpServletRequest request){
        return eventService.scrapEvent(index,event,request);
    }
}
