package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.ItemDao;
import com.liwinon.itevent.dao.SapDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    SapDao sapDao;
    @Autowired
    ItemDao itemDao;
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
}
