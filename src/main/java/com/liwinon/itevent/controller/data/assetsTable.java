package com.liwinon.itevent.controller.data;

import com.liwinon.itevent.annotation.PasssToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/itevent")
public class assetsTable {

    @GetMapping(value = "/assetsTable")
    @PasssToken
    public String assetsTable(){
        return "data/assetsTable";
    }
}
