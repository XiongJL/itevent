package com.liwinon.itevent.controller.data;

import com.liwinon.itevent.annotation.PasssToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据调用接口请查看apiController
 */
@Controller
@RequestMapping("/itevent")
public class AssetsTableController {

    @GetMapping(value = "/assetsTable")
    @PasssToken
    public String assetsTable(){
        return "data/assetsTable";
    }

}
