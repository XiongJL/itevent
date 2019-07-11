package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.AssetsDao;
import com.liwinon.itevent.dao.EventDao;
import com.liwinon.itevent.dao.ItemDao;
import com.liwinon.itevent.dao.SapDao;
import com.liwinon.itevent.entity.Assets;
import com.liwinon.itevent.entity.BackModel;
import com.liwinon.itevent.entity.Event;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    SapDao sapDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    AssetsDao assetsDao;
    /**
     * 返回JSON格式的 姓名和部门
     * @param userid
     * @return
     */
    @Override
    public JSONObject getNameDepart(String userid) {
        String result = sapDao.findNDByUserId(userid);
        if (!StringUtils.isEmpty(result)){
            String[] res = result.split(",");
            JSONObject json = new JSONObject();
            if (res.length==2){
                json.accumulate("name",res[0]);
                json.accumulate("department",res[1]);
                System.out.println(json);
                return json;
            }
        }
        return null;
    }

    @Override
    public List<String> getTypes() {
        return  itemDao.findAllTypes();
    }

    @Override
    public List<String> getBrands(String type) {
        return itemDao.brandByType(type);
    }

    @Override
    public String getUnit(String type) {
        return itemDao.unitByType(type);
    }
    //获取快借用逾期的资产
    public List<Assets> colseAssets(){
        //查询7天内即将逾期的且是在外借用状态的事件
        List<Event> list = eventDao.findCloseEvent();
        //再去找Assets状态是在外的 , 可用的
        if (list.size()>0){
            List<Assets> res = new ArrayList<>();
            for (Event e : list){
                List<Assets> tmp =  assetsDao.findByUseridBorrow(e.getUserid());
                res.addAll(tmp);
            }
            return res;
        }
        return null;
    }

    @Override
    public List<BackModel> whoAllUsed(String userid) {
        return assetsDao.findUseByBack(userid);
    }
}
