package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.AccessDao;
import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.ItemDao;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.*;
import com.liwinon.itevent.entity.Model.ATableModel;
import com.liwinon.itevent.entity.Model.BackModel;
import com.liwinon.itevent.entity.Model.ETableModel;
import com.liwinon.itevent.entity.primary.Access;
import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.Item;
import com.liwinon.itevent.entity.second.Sap_Users;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    AccessDao accessDao;
    /**
     * 返回JSON格式的 姓名和部门和手机
     * @param userid
     * @return
     */
    @Override
    public JSONObject getUserInfoById(String userid) {
        String result = sapDao.findNDByUserId(userid);
        if (!StringUtils.isEmpty(result)){
            String[] res = result.split(",");
            JSONObject json = new JSONObject();
            if (res.length==3){
                json.accumulate("name",res[0]);
                json.accumulate("department",res[1]);
                json.accumulate("telephone",res[2]);
                System.out.println(json);
                return json;
            }
        }
        return null;
    }
    /**根据姓名查找*/
    @Override
    public JSONObject getUserInfoByName(String name) {
        List<Sap_Users> result = sapDao.findNDByName(name);
        if (result.size()>0){
            JSONObject json = new JSONObject();
            json.accumulate("nums",result.size());
            JSONArray arr = new JSONArray();
            for (Sap_Users sap : result){
                JSONObject data = new JSONObject();
                data.accumulate("userid",sap.getPERSONID());
                data.accumulate("department",sap.getORGTXT());
                data.accumulate("telephone",sap.getTELEPHONE());
                arr.add(data);
            }
            json.accumulate("data",arr);
            return json;
        }
        return null;
    }

    /**
     * 根据部门,姓名查找唯一信息
     *  如果部门一样还姓名一样,????再说
     * @param dep
     * @param name
     * @return
     */
    @Override
    public JSONObject getIdPhone(String dep, String name) {
        Sap_Users result =  sapDao.findIDPhoneByDepName(dep,name);
        if (result!=null){
            JSONObject json = new JSONObject();
            json.accumulate("userid",result.getPERSONID());
            json.accumulate("telephone",result.getTELEPHONE());
            return json;
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
        return itemDao.unitByType(type).get(0);
    }
    //获取快借用逾期的资产
    public List<Assets> colseAssets(){
        //查询7天内即将逾期的且是在外借用状态的事件
        List<Event> list = eventDao.findCloseEvent();
        //再去找Assets状态是在外的 , 可用的
        if (list.size()>0){
            List<Assets> res = new ArrayList<>();
            for (Event e : list){
                Access access =  accessDao.findByEventid(e.getUuid());
                if (access==null)
                    continue;
                Assets tmp =  assetsDao.findById(access.getAid());
                res.add(tmp);
            }
            return res;
        }
        return null;
    }

    @Override
    public List<BackModel> whoAllUsed(String userid) {
        return assetsDao.findUseByBack(userid);
    }

    /**
     * 获取assetsTable的分页数据
     * @param pageNumber 前端页数从1开始, 后端从0开始
     * @param limit  每页数量
     * @param search  查询内容
     * @return
     */
    @Override
    public JSONObject assetsTable(int pageNumber, int limit, String search,String date1,String date2) {
        if (pageNumber>0){
            pageNumber = pageNumber-1;
        }

        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(pageNumber,limit,sort);
        Page<Assets> page = null;
        if (!StringUtils.isEmpty(search)){
            search = search.trim();
            JSONObject json = new JSONObject();
            if ("逾期资产".equals(search)){
                List<Assets> data = colseAssets();
                if (data!=null){
                    List<ATableModel> res = getTableModel(data);
                    json.accumulate("code",0);
                    json.accumulate("msg","ok");
                    json.accumulate("count",data.size());
                    json.accumulate("data",res);
                    return json;
                }else{
                    json.accumulate("code",1); //无数据
                    json.accumulate("msg","无逾期资产");
                    json.accumulate("count",0);
                    json.accumulate("data",new ArrayList<ATableModel>());
                    return json;
                }
            }
            else if (!StringUtils.isEmpty(date1)&& !StringUtils.isEmpty(date2)){
                date1 = date1.trim();
                date2 = date2.trim();
                page = assetsDao.findAllBySearchAndDate(search,date1,date2,pageable);
            }else{
                page  = assetsDao.findAllBySearch(search,pageable);
            }
        } else if (!StringUtils.isEmpty(date1)&& !StringUtils.isEmpty(date2)){  //只查询时间区间
            date1 = date1.trim();
            date2 = date2.trim();
            page = assetsDao.findAllByDate(date1,date2,pageable);
        }
        else{
            page = assetsDao.findAll(pageable);
        }
        List<Assets> data = page.getContent();
        List<ATableModel> res = getTableModel(data);
        JSONObject json = new JSONObject();
        json.accumulate("code",0);
        json.accumulate("msg","ok");
        json.accumulate("count",page.getTotalElements());
        json.accumulate("data",res);
        System.out.println(json);
        return json;
    }
    /**
     * 获取eventTable的分页数据
     * @param pageNumber 前端页数从1开始, 后端从0开始
     * @param limit  每页数量
     * @param search  查询内容
     * @return
     */
    @Override
    public JSONObject eventTable(int pageNumber, int limit, String search,String date1,String date2) {
        if (pageNumber>0){
            pageNumber = pageNumber-1;
        }

        Sort sort = new Sort(Sort.Direction.ASC,"uuid");
        Pageable pageable = PageRequest.of(pageNumber,limit,sort);
        Page<Event> page = null;
        if (!StringUtils.isEmpty(search)){
            search = search.trim();
            if (EventEnum.getId(search)!=null){
                //可以找到对应的事件说明是查询事件
                if (!StringUtils.isEmpty(date1)&& !StringUtils.isEmpty(date2)){ //同时根据日期查询
                    date1 = date1.trim();
                    date2 = date2.trim();
                    page = eventDao.findByEventAndDate(EventEnum.getId(search),date1,date2,pageable);
                }else{
                    page = eventDao.findByEvent(EventEnum.getId(search),pageable);
                }
            }else{ //查询其他
                if (!StringUtils.isEmpty(date1)&& !StringUtils.isEmpty(date2)){ //同时根据日期查询
                    date1 = date1.trim();
                    date2 = date2.trim();
                    page  = eventDao.findMostBySearchAndDate(search,date1,date2,pageable);
                }else{
                    page  = eventDao.findMostBySearch(search,pageable);
                }
            }
        }else if (!StringUtils.isEmpty(date1)&& !StringUtils.isEmpty(date2)){
            date1 = date1.trim();
            date2 = date2.trim();
            page = eventDao.findAllByDate(date1,date2,pageable);
        }
        else{
            page = eventDao.findAll(pageable);
        }
        //转换为表模型类
        List<Event> data = page.getContent();
        List<ETableModel> res = getEventTable(data);
        JSONObject json = new JSONObject();
        json.accumulate("code",0);
        json.accumulate("msg","ok");
        json.accumulate("count",page.getTotalElements());
        json.accumulate("data",res);
        System.out.println(json);
        return json;
    }



    /**
     * 获取物料对应的库存数量和库存可用数量
     * @param type
     * @param brand
     * @return
     */
    @Override
    public int[] getNumbers(String type, String brand) {
        Item item =  itemDao.findByTypeAndBrand(type,brand);
        if (item==null){
            return null;
        }
        //可用的数量
        int unuse = (int)assetsDao.countAssetsByItemid(item.getItemid());
        // 全部的数量
        int all = (int)assetsDao.countAllAssetsByItemid(item.getItemid());
        return new int[]{all,unuse};
    }

    /**
     *  根据物料编码获取类型和型号
     * @param itemid 物料编码
     * @return
     */
    @Override
    public String[] getTypeAndBrand(String itemid) {
        Item item =  itemDao.findByItemid(itemid);
        if (item==null){
            return null;
        }
        String[] strs = new String[]{item.getType(),item.getBrand()};
        return  strs;
    }

    /**
     * 根据类型型号获取物料编码
     * @param type
     * @param brand
     * @return
     */
    @Override
    public String getItemId(String type, String brand) {
        Item item =  itemDao.findByTypeAndBrand(type,brand);
        if (item==null){
            return null;
        }
        return item.getItemid();
    }

    /**
     * 获取资产总数,即用数量,报废数,物料种类
     * @return
     */
    @Override
    public JSONObject getAssetsNums() {
        JSONObject json = new JSONObject();
        json.accumulate("total",assetsDao.count());
        json.accumulate("available",assetsDao.countAllAvailable());
        json.accumulate("zichanRepo",assetsDao.countZichanRepoNum());
        json.accumulate("feiyongRepo",assetsDao.countFeiyongRepoNum());
        json.accumulate("scrap",assetsDao.countScrapNum());
        json.accumulate("itemNum",itemDao.countItemType());
        return json;
    }

    @Override
    //获取某一年12个月的事件数量
    public JSONObject eventByYear(String year) {
        List<String[]> res =  eventDao.countMothOfYear(Integer.valueOf(year));
        if (res==null)
            return null;
        JSONObject json = new JSONObject();
        for (String[] obj:res){
            //str = { 年,月,事件数}  例如 {2019,5,60}
            json.accumulate(obj[1],Integer.valueOf(obj[2]));
        }
        return json;
    }

    //返回总数
    public long countEvent(){
        return eventDao.count();
    }

    @Override
    public long countAssets() {
        return assetsDao.count();
    }

    /**
     * 转换查询到的资产数据为表格模型数据
     * @param data
     * @return
     */
    public List<ATableModel> getTableModel(List<Assets> data){
        if (data!=null){


        List<ATableModel> res = new ArrayList<>(data.size());
        for (Assets a : data){
            String itemid = a.getItemid();
            Item item = itemDao.findByItemid(itemid);
            String name = "无使用者";
            String phone = "无联系方式";
            if(!StringUtils.isEmpty(a.getUserid())){
                name = sapDao.findNByUserId(a.getUserid());
                for (String s : eventDao.findPhoneByUserid(a.getUserid())){
                    if (!StringUtils.isEmpty(s)){
                        phone = s;
                    }
                }
            }
            String state = "";
            if (a.getState()==0){
                state = "在库";
            }else if (a.getState()==1){
                state = "在外";
            }else if (a.getState()==2){
                state = "借用中";
            }else if (a.getState()==3){
                state = "已报废";
            }
            String store = "";
            if (a.getStore()==0){
                store = "资产仓";
            }else{
                store = "费用仓";
            }
            Date date = a.getBuyDate();
            String date2 = "";
            if (date!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date2 = sdf.format(date);
            }
            ATableModel aTableModel = new ATableModel(a.getId(),a.getAssetsid(),name,a.getUserid(),phone,item.getType(),item.getBrand(),state,store,date2) ;
            res.add(aTableModel);
        }
         return res;
        }else{
            return null;
        }
    }
    /**
     * 转换查询到的事件数据为表格模型
     * 更新版,使用枚举完成
     */
    public List<ETableModel> getEventTable(List<Event> data){
        List<ETableModel> res = new ArrayList<>(data.size());
        for (Event e : data){
            String event = EventEnum.getName(e.getEvent());
            Date date1 = e.getDate();
            Date date2 = e.getApplydate();
            String date1s = "";
            String date2s = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date1!=null){
                date1s = sdf.format(date1);
            }if (date2!=null){
                date2s = sdf.format(date2);
            }
            res.add(new ETableModel(e.getUuid(),event,e.getItemid(),e.getCount(),e.getUnit(),e.getUserid(),
                    e.getPhone(),e.getAdminuser(),date1s,date2s,e.getOaid(),e.getOrderid()));
        }
        return res;
    }

}
