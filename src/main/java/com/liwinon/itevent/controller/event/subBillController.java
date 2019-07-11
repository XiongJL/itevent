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

/**
 *  提单领取 , 新员工领取
 */
@Controller
@RequestMapping(value = "/itevent")
public class subBillController {
    @Autowired
    EventService eventService;
    @GetMapping(value = "/subBill")
    @PasssToken
    public String subBill(){
        return "event/subBill";
    }

    /**
     * 提单领取事件
     * @param index 一共有多少种物料
     * @param event 事件编号
     * @return
     */
    @PostMapping(value = "/startBillEvent")
    @ResponseBody
    public String startBillEvent(int index, int event,HttpServletRequest request){
        System.out.println(index);
        System.out.println(event);
        return eventService.startBillEvent(index,event,request);
    }



}
