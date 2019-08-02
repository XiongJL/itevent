package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/itevent")
public class borrowController {
    @Autowired
    EventService eventService;
    @GetMapping(value = "/borrow")
    @PasssToken
    public String borrow(){
        return "event/borrow";
    }
    @PostMapping(value = "/borrowEvent")
    @ResponseBody
    public String borrowEvent(int index, int event, HttpServletRequest request){
        return eventService.borrowEvent(index, event, request);
    }
}
