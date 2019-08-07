package com.liwinon.itevent.service;

import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.entity.Model.BackModel;
import net.sf.json.JSONObject;

import java.util.List;

public interface ApiService {
    JSONObject getUserInfoById(String userid);
    JSONObject getUserInfoByName(String name);
    JSONObject getIdPhone(String dep, String name);

    List<String> getTypes();
    List<String> getBrands(String type);
    String getUnit(String type);
    //获取快借用逾期的资产
    List<Assets> colseAssets();
    //获取使用者所有使用中的资产
    List<BackModel> whoAllUsed(String userid);

    //获取assetsTable的分页数据
    JSONObject assetsTable(int pageNumber,int limit,String search,String date1,String date2);
    //获取eventTable的分页数据
    JSONObject eventTable(int pageNumber, int limit, String search,String date1,String date2);
    //获取物料对应的库存数量和库存可用数量
    int[] getNumbers(String type, String brand);
    //根据物料编码获取类型和型号
    String[] getTypeAndBrand(String itemid);
    //根据类型型号获取物料编码
    String getItemId(String type, String brand);

    //获取资产总数,即用数量,报废数,物料种类
    JSONObject getAssetsNums();

    //获取某一年12个月的事件数量
    JSONObject eventByYear(String year);

    //获取事件总数
    long countEvent();
    //获取资产总数
    long countAssets();

}
