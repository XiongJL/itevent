package com.liwinon.itevent.controller;

import com.liwinon.itevent.annotation.PasssToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  提单领取
 */
@Controller
@RequestMapping(value = "/itevent")
public class subBillController {
    @GetMapping(value = "/subBill")
    @PasssToken
    public String subBill(){
        return "event/subBill";
    }
}
