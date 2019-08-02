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
public class echangeController {
    @Autowired
    EventService eventService;
    /**
     * 以旧换新
     */
    @GetMapping(value = "/exchange")
    @PasssToken
    public String exchange(){
        return "event/exchange";
    }
    @PostMapping(value = "/exchangeEvent")
    @ResponseBody
    public String exchangeEvent(int index, int event, HttpServletRequest request){

        return eventService.exchangeEvent(index, event, request);
    }
}
