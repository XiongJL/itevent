package com.liwinon.itevent.controller;

import com.liwinon.itevent.service.ApiService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/itevent/api")
public class apiController {
    @Autowired
    ApiService api;
    @GetMapping(value = "/getNameDepart")
    public JSONObject getNameDepart(String userid){
        return api.getNameDepart(userid);
    }

    //获取类型
    @GetMapping(value = "/getTypes")
    public List<String> types(){

        return api.getTypes();
    }
    //获取品牌型号
    @GetMapping(value = "/getBrands")
    public List<String> brands(String type){
        return api.getBrands(type);
    }
    //获取单位
    @GetMapping(value = "/getUnit")
    public String unit(String type){
        return api.getUnit(type);
    }
}
