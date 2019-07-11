package com.liwinon.itevent.service;

import com.liwinon.itevent.entity.Assets;
import com.liwinon.itevent.entity.BackModel;
import net.sf.json.JSONObject;

import java.util.List;

public interface ApiService {
    JSONObject getNameDepart(String userid);
    List<String> getTypes();
    List<String> getBrands(String type);
    String getUnit(String type);
    //获取快借用逾期的资产
    List<Assets> colseAssets();
    //获取使用者所有使用中的资产
    List<BackModel> whoAllUsed(String userid);
}
