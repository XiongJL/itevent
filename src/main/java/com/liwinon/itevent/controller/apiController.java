package com.liwinon.itevent.controller;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.Model.BackModel;
import com.liwinon.itevent.entity.second.Sap_Users;
import com.liwinon.itevent.service.ApiService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/itevent/api")
public class apiController {
    @Autowired
    ApiService api;
    @Autowired
    SapDao sapDao;
    @GetMapping(value = "/getNameDepart")
    public JSONObject getNameDepart(String userid){
        return api.getUserInfoById(userid);
    }
    @GetMapping(value = "/getIdDep")
    public JSONObject getIdDep(String name){
        return api.getUserInfoByName(name);
    }
    @GetMapping(value = "/getIdPhone")
    public JSONObject getIdPhone(String dep,String name){
        return api.getIdPhone(dep,name);
    }
    //获取类型
    @GetMapping(value = "/getTypes")
    public List<String> types(){
        return api.getTypes();
    }
    //获取物料编码
    @GetMapping(value="/getItemId")
    public String itemid(String type,String brand){
        return api.getItemId(type,brand);
    }
    //获取品牌型号
    @GetMapping(value = "/getBrands")
    public List<String> brands(String type){
        return api.getBrands(type);
    }

    //获取物料对应的库存数量和库存可用数量
    @GetMapping(value = "/getNumbers")
    public int[] getNumbers(String type,String brand){
        return api.getNumbers(type,brand);
    }
    //根据物料编码获取类型和型号
    @GetMapping(value = "/getTypeAndBrand")
    public String[] getTypeAndBrand(String itemid){
        return api.getTypeAndBrand(itemid);
    }
    //获取单位
    @GetMapping(value = "/getUnit")
    public String unit(String type){
        return api.getUnit(type);
    }
    
    //获取有没有名字
    @GetMapping(value = "/getNamePersonid")
    @PasssToken
    public JSONObject getNamePersonid(HttpServletRequest request){
    	String userid=request.getParameter("userid").toString();
    	String data=sapDao.findNByUserId(userid);
    	JSONObject json=new JSONObject();
		json.accumulate("code",200);
        json.accumulate("data",data);
		return  json;
    }


    //获取使用者的所有资产
    @GetMapping(value = "/whoAllUsed")
    public List<BackModel> whoAllUsed(String userid){
        return api.whoAllUsed(userid);
    }

    //获取资产分页表格数据
    @GetMapping(value = "/assetsTable")
    public JSONObject assetsTable(int page,int limit,String search,String date1,String date2){
        System.out.println("第几页:"+page);
        System.out.println("每页条数:"+limit);
        System.out.println("查询参数:"+search);
        return api.assetsTable(page,limit,search,date1,date2);
    }
    //获取事件分页表格数据
    @GetMapping(value = "/eventTable")
    public JSONObject eventTable(int page,int limit,String search,String date1,String date2){
        System.out.println("第几页:"+page);
        System.out.println("每页条数:"+limit);
        System.out.println("查询参数:"+search);
        return api.eventTable(page,limit,search,date1,date2);
    }
    //获取事件全部表格数据
    @GetMapping(value = "/eventAllTable")
    public JSONObject eventAllTable(){
        int page = 1;
        int limit = (int)api.countEvent();
        return api.eventTable(page,limit,null,null,null);
    }
    //获取事件全部表格数据
    @GetMapping(value = "/assetsAllTable")
    public JSONObject assetsAllTable(){
        int page = 1;
        int limit = (int)api.countAssets();
        return api.assetsTable(page,limit,null,null,null);
    }

    //获取主页图表chart1 数据
    @GetMapping(value = "/chart1")
    public JSONObject chart1(String year){
        return api.eventByYear(year);
    }
}
