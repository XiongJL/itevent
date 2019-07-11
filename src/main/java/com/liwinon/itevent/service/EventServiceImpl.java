package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.AccessDao;
import com.liwinon.itevent.dao.AssetsDao;
import com.liwinon.itevent.dao.EventDao;
import com.liwinon.itevent.dao.ItemDao;
import com.liwinon.itevent.entity.Access;
import com.liwinon.itevent.entity.Assets;
import com.liwinon.itevent.entity.Event;
import com.liwinon.itevent.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    EventDao eventDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    AssetsDao assetsDao;
    @Autowired
    AccessDao accessDao;
    /**
     * 开始提单领取事件
     * @param index
     * @param event
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String startBillEvent(int index, int event, HttpServletRequest request) {
       return subBill(index, event, request);
    }

    /**
     *  新资产入库事件
     *  03 添加新的资产   08 添加的 Store为 1 费用仓
     * @param index  物料有多少种
     * @param event  03
     * @param request
     * @param exist    是否是已有但未记录的资产, 是1  ,否0
     * @return
     */
    @Override
    @Transactional
    public String newAssetsEvent(int index, int event,int exist, HttpServletRequest request) {
        String remark = request.getParameter("remark");
        if (StringUtils.isEmpty(remark)){
            remark = "";
        }
        System.out.println("备注:"+remark);
        if (exist==1){  // 是已有资产,但未在系统中记录
            event = 8;
        }
        System.out.println(index);
        System.out.println(event);
        System.out.println(exist);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+event;
        // List<Event> list = new ArrayList<>();
        /**
         * 前端固定最多20个 , 循环20次的原因是, 不确定用户删除某计列后, 命名不是按照顺序的
         *  例如 type1 type3 type5 用index 数量去循环 就只能得到 type1 tpye2 type3.
         */
        int eventTime = 1;
        for (int i = 1; i<=20; i++){
            String type = request.getParameter("type"+i);
            System.out.println(type);
            if (StringUtils.isEmpty(type)){
                continue;
            }
            String uuid = uuid1;
            uuid = uuid+"-"+eventTime;
            eventTime ++;
            String assetsid = request.getParameter("assetsid"+i);
            String brand = request.getParameter("brand"+i);
            int count = Integer.valueOf(request.getParameter("count"+i));
            String unit = request.getParameter("unit"+i);
            //查询物料ID
            Item item =  itemDao.findByTypeAndBrand(type,brand);
            String itemid = item.getItemid();
            if (StringUtils.isEmpty(itemid)){
                return "err,物料错误";
            }
            //获取操作人员
            HttpSession session = request.getSession();
            String adminuser = (String)session.getAttribute("username");

            Event e = new Event();
            e.setUuid(uuid);
            e.setItemid(itemid);
            e.setEvent(event);
            e.setCount(count);
            e.setUnit(unit);
            e.setDate(date);
            e.setAdminuser(adminuser);
            e.setRemark(remark);
            System.out.println("e:"+e);
            //所有准备入的资产
            List<Assets> in = new ArrayList<>(count);
            //如果有assetsid , 首先尝试找该资产
            if (!StringUtils.isEmpty(assetsid)){
                Assets a = assetsDao.findByAssetsid(assetsid);
                if (a!=null){
                    in.add(a);
                    count--;
                }else{  // 没有找到但是又有资产牌 , 后面根据事件不同设置不同状态值
                    a = new Assets();
                    a.setAssetsid(assetsid);
                    a.setItemid(itemid);
                    a.setRemark(remark);
                    in.add(a);
                    count--;
                }
            }
            if (count>0){  //入库数量大于0
                for (int c=0;c<count;c++){
                    Assets assets = new Assets();
                    assets.setItemid(itemid);
                    in.add(assets);
                }
            }
            //保存事件
            eventDao.save(e);
            //更改出库的状态 , 并添加出入记录
            if (in.size()>0){
                for (int z=0;z<in.size();z++){
                    Assets a = in.get(z);
                    //
                    if (event==8){
                        //设置仓为 1 , 否则是新资产设为0
                        a.setStore(1);
                    }else {
                        a.setStore(0);  //资产仓
                        a.setBuyDate(date);  // 03事件才设置购买日期
                    }
                    a.setState(0); //在库
                    assetsDao.save(a);
                    Access access = new Access();
                    access.setAid(a.getId());
                    access.setEventid(uuid);
                    accessDao.save(access);
                }
            }
        }
        return "ok";
    }

    /**
     * 物料添加
     * @param index
     * @param event
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String addItemEvent(int index, int event, HttpServletRequest request) {

        System.out.println(index);
        System.out.println(event);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+event;
        // List<Event> list = new ArrayList<>();
        /**
         * 前端固定最多20个 , 循环20次的原因是, 不确定用户删除某计列后, 命名不是按照顺序的
         *  例如 type1 type3 type5 用index 数量去循环 就只能得到 type1 tpye2 type3.
         */
        int eventTime = 1;
        for (int i = 1; i<=20; i++){
            String type = request.getParameter("type"+i);
            System.out.println(type);
            if (StringUtils.isEmpty(type)){
                continue;
            }
            String itemid = request.getParameter("itemid"+i);
            if (StringUtils.isEmpty(itemid)){
                return "物料编码不可为空";
            }
            itemid = itemid.trim();
            Item item = itemDao.findByItemid(itemid);
            System.out.println("item是:"+item);
            if (item!=null){
                return itemid+"物料已存在";
            }
            String uuid = uuid1;
            uuid = uuid+"-"+eventTime;
            eventTime ++;

            String brand = request.getParameter("brand"+i);
            String unit = request.getParameter("unit"+i);
            item = new Item();
            item.setType(type);
            item.setBrand(brand);
            item.setUnit(unit);
            item.setItemid(itemid);
            System.out.println("准备保存的item:"+item);
            //获取操作人员
            HttpSession session = request.getSession();
            String adminuser = (String)session.getAttribute("username");
            Event e = new Event();
            e.setUuid(uuid);
            e.setItemid(itemid);
            e.setEvent(event);
            e.setUnit(unit);
            e.setDate(date);
            e.setAdminuser(adminuser);
            System.out.println("e:"+e);

            //保存事件
            eventDao.save(e);
            //保存物料
            itemDao.save(item);
            //不需要出入记录
        }
        return "ok";
    }

    /**
     * 以旧换新
     * @param index
     * @param event  01
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String exchangeEvent(int index, int event, HttpServletRequest request) {
        // yyyyMMdd-envent-01 年月日event一样说明是同一事件,流水号说明该事件第几种物料
        String userid = request.getParameter("userid");
        if (StringUtils.isEmpty(userid)){
            return "EmptyUserid";
        }
        //String name = request.getParameter("name").trim();
        String remark = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+event;
        int eventTime = 1;
        for (int i = 1; i<=20; i++){
            String type = request.getParameter("type"+i);
            if (StringUtils.isEmpty(type)){
                continue;
            }
            System.out.println(type);
            System.out.println("更换的类型:"+type);
            String extype = request.getParameter("extype"+i);
            String uuid = uuid1;
            String uuidNew = uuid1;
            uuid = uuid+"-"+eventTime+"-old";
            uuidNew = uuidNew+"-"+eventTime+"-new";
            eventTime ++;
            String assetsid = request.getParameter("assetsid"+i);
            String exassetsid = request.getParameter("exassetsid"+i);
            String brand = request.getParameter("brand"+i);
            String exbrand = request.getParameter("exbrand"+i);
            int count = Integer.valueOf(request.getParameter("count"+i));
            int excount = Integer.valueOf(request.getParameter("excount"+i));
            String unit = request.getParameter("unit"+i);
            String exunit = request.getParameter("exunit"+i);
            //是否废弃
            int bad = 0;
            if (request.getParameter("bad"+i)!=null){
                bad  = Integer.valueOf(request.getParameter("bad"+i));
            }
            //根据type , brand 去找物料ID , 根据count bad 去Assets资产表中更改字段,
            // 如果有assetsid,还要在出入的资产中添加进去.
            // 然后再去Access出入表中记录
            //查询物料ID
            Item item =  itemDao.findByTypeAndBrand(type,brand);
            Item exitem =  itemDao.findByTypeAndBrand(extype,exbrand);
            //获取操作人员
            HttpSession session = request.getSession();
            String adminuser = (String)session.getAttribute("username");
            Event e = new Event(uuid,event,item.getItemid(),count,unit,userid,adminuser,date,remark);
            Event ex = new Event(uuidNew,event,exitem.getItemid(),excount,exunit,userid,adminuser,date,remark);
            System.out.println("e:"+e);
            System.out.println("ex:"+e);
            //所有准备换入的资产
            List<Assets> in = new ArrayList<>(count);
            //准备换出的
            List<Assets> out = new ArrayList<>(excount);
            //如果有assetsid , 首先找该资产
            if (!StringUtils.isEmpty(assetsid)){
                Assets a = assetsDao.findByAssetsid(assetsid);
                if (a!=null){
                    in.add(a);
                    if (a.getState()==0){
                        return assetsid+"资产已在仓库!";
                    }
                    count--;
                }
            }
            if (!StringUtils.isEmpty(exassetsid)){
                Assets a = assetsDao.findByAssetsid(exassetsid);
                if (a!=null){
                    out.add(a);
                    if (a.getState()==1||a.getState()==2){
                        return exassetsid+"资产不在仓库!";
                    }
                    excount--;
                }
            }
            //需求数量还不为0
            if (count>0){
                //根据物料编码 查找在外的资产
                List<Assets> list1 = assetsDao.finduseByItemid(item.getItemid());
                if (list1.size()>=count){  //数量够
                    for (int c = 0;c<count;c++){
                        if (list1.get(c)!=null){
                            in.add(list1.get(c));
                        }else {
                            System.out.println("出现错误! 资产为null");
                        }
                    }
                } else {
                    //数量不够  数据库内未找到足够数量匹配退还的资产,请先做新资产入库,并开启 已有,但未记录
                    return "资产数量不够,请先将准备退还的资产做新资产入库";
                }
            }
            //换新数量还不为0
            if (excount>0){
                Map<String,List<Assets>> res = outList(excount,exitem.getItemid());
                if (res.get("ok")==null){
                    //回滚事务
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return type+"数量不足!";
                }
                List<Assets> tmp = res.get("ok");
                out.addAll(tmp);
            }
            //保存事件
            eventDao.save(e);
            eventDao.save(ex);
            //更改出库的状态 , 并添加出入记录
            if (out.size()>0){
                for (int z=0;z<out.size();z++){
                    Assets a = out.get(z);
                    //如果exassetsid存在且不在库中,说明是出库时贴的资产牌, 在out list中,随便扣一个贴上
                    if (!StringUtils.isEmpty(exassetsid)){
                        //exassetsid放入第一个同类资产
                        if (z==0){
                            a.setAssetsid(exassetsid);
                        }
                    }
                    a.setUserid(userid);  // 添加使用者工号
                    a.setStore(1);  //设为费用仓
                    a.setState(1);  //设为出库状态
                    assetsDao.save(a);
                    Access access = new Access();
                    access.setAid(a.getId());
                    access.setEventid(uuidNew);
                    accessDao.save(access);
                }
            }
            //入库状态 , 判断是否是报废
            if (in.size()>0){
                for (int z=0;z<in.size();z++){
                    Assets a = in.get(z);
                    //如果assetsid存在且不在库中,说明是出库时贴的资产牌, 在out list中,随便扣一个贴上
                    if (!StringUtils.isEmpty(assetsid)){
                        //assetsid放入第一个同类资产
                        if (z==0){
                            a.setAssetsid(assetsid);
                        }
                    }
                    a.setUserid(""); //把使用者置为空
                    a.setStore(1);  //设为费用仓
                    a.setState(0);  //设为入库状态
                    if (bad==1){  //是报废的
                        a.setState(3);
                    }
                    assetsDao.save(a);
                    Access access = new Access();
                    access.setAid(a.getId());
                    access.setEventid(uuid);
                    accessDao.save(access);
                }
            }

        }
        return "ok";


    }

    /**
     * 借出资产
     * @param index
     * @param event
     * @param request
     * @return
     */
    @Override
    public String borrowEvent(int index, int event, HttpServletRequest request) {

        return subBill(index, event, request);
    }

    /**
     * 归还资产
     * @param event  04
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String backEvent(int index,int event, HttpServletRequest request) {
        String userid =  request.getParameter("userid");
        if (StringUtils.isEmpty(userid)){
            return "工号不能为空";
        }
        HttpSession session = request.getSession();
        String adminuser = (String) session.getAttribute("username");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid = sdf.format(date)+"-"+event;
        int eventTime = 1;
        //根据tr数量循环
        for (int i = 1 ;i<=index;i++ ){
            String back = request.getParameter("back"+i);
            if (StringUtils.isEmpty(back)){  //如果该列没有被勾选,不处理
                continue;
            }
            int aid = Integer.valueOf(request.getParameter("aid"+i));
            String assetsid = request.getParameter("assetsid"+i);
            String type = request.getParameter("type"+i);
            String brand = request.getParameter("brand"+i);
            int count = Integer.valueOf(request.getParameter("count"+i));
            String unit = request.getParameter("unit"+i);
            // 根据aid 去查找对应的资产
            Assets a =  assetsDao.findById(aid);
            a.setUserid("");  // 使用者置为空
            a.setState(0); //状态置为在库
            a.setStore(1);  //置为费用仓
            assetsDao.save(a);
            //记录事件
            String uuidtmp = uuid+"-"+eventTime;
            eventTime ++;
            Event e = new Event(uuid,event,a.getItemid(),count,unit,userid,adminuser,date,"");
            eventDao.save(e);
            //记录出入
            Access access = new Access();
            access.setEventid(uuidtmp);
            access.setAid(aid);
            accessDao.save(access);
        }
        return "ok";
    }

    /**
     * 报废事件
     * @param index
     * @param event  2
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String scrapEvent(int index, int event, HttpServletRequest request) {
        // yyyyMMddenvent-01 年月日event一样说明是同一事件,流水号说明该事件第几个物料
        String userid = request.getParameter("userid");
        if (StringUtils.isEmpty(userid)){
            return "EmptyUserid";
        }
        //String name = request.getParameter("name").trim();
        String remark = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+event;


        /**
         * 前端固定最多20个 , 循环20次的原因是, 不确定用户删除某计列后, 命名不是按照顺序的
         *  例如 type1 type3 type5 用index 数量去循环 就只能得到 type1 tpye2 type3.
         */
        int eventTime = 1;
        for (int i = 1; i<=20; i++){
            System.out.println(i);
            String type = request.getParameter("type"+i);
            System.out.println(type);
            if (StringUtils.isEmpty(type)){
                continue;
            }
            String uuid = uuid1;
            uuid = uuid+"-"+eventTime;
            eventTime ++;
            String assetsid = request.getParameter("assetsid"+i);
            String brand = request.getParameter("brand"+i);
            int count = Integer.valueOf(request.getParameter("count"+i));
            String unit = request.getParameter("unit"+i);
            //查询物料ID
            Item item =  itemDao.findByTypeAndBrand(type,brand);
            String itemid = item.getItemid();
            //所有准备报废的资产
            List<Assets> out = new ArrayList<>(count);
            //如果有assetsid , 首先找该资产
            if (!StringUtils.isEmpty(assetsid)){
                Assets a = assetsDao.findByAssetsid(assetsid);
                if (a!=null){
                    if (a.getState()==1||a.getState()==2){
                        return assetsid+"资产不在仓库!";
                    }
                    if (a.getState()==3){
                        return "资产牌:"+assetsid+"已经报废!";
                    }
                    out.add(a);
                    count--;
                }
            }
            //查询当前用户的使用资产中是否有该报废类型的资产
            List<Assets> same = assetsDao.findByUseridAndItemid(userid,itemid);
            if (same!=null && same.size()>0){
                for (Assets a : same){
                    if(count<=0){
                        break;
                    }
                    if (a.getAssetsid().equals(assetsid)){
                        //如果是上面已经指定的资产id,则跳过, 但通常不会进入此方法
                        //除非操作人员填写了资产ID,又把数量设置了 1 以上
                        break;
                    }
                    out.add(a); //添加到报废组中
                    count--;
                }
            } else {
                return "该用户没有使用"+type+brand+"的资产 "+ count+ unit;
            }
            if (count >0){  //报废数量还是超过了使用的数量
                return "该用户没有使用"+type+brand+"的资产 "+ count+ unit;
            }
            //获取操作人员
            HttpSession session = request.getSession();
            String adminuser = (String)session.getAttribute("username");
            Event e = new Event(uuid,event,itemid,count,unit,userid,adminuser,date,remark);
            //保存事件
            eventDao.save(e);
            //更改资产为报废 ,记录出入记录
            if (out.size()>0){
                for (int z=0;z<out.size();z++){
                    Assets a = out.get(z);
                    //如果assetsid存在且不在库中,说明是出库时贴的资产牌, 在out list中,随便扣一个贴上
                    if (!StringUtils.isEmpty(assetsid)){
                        //assetsid放入第一个同类资产
                        if (z==0){
                            a.setAssetsid(assetsid);
                        }
                    }
                    a.setUserid(userid);
                    a.setStore(1);  //设为费用仓
                    a.setState(3);  //设为报废状态
                    assetsDao.save(a);
                    Access access = new Access();
                    access.setAid(a.getId());
                    access.setEventid(uuid);
                    accessDao.save(access);
                }
            }
        }
        return "ok";
    }


    /**
     * 优先判断准备出库的资产 在费用仓是否足够,接着再判断资产仓的
     * 根据事件类型返回不同结果
     * @param count
     * @param itemid
     * @return
     */
    public Map<String,List<Assets>> outList(int count, String itemid){
        Map<String,List<Assets>> result = new HashMap();
        List<Assets> out = new ArrayList<>(count);
        //换新数量还不为0
        if (count>0){
            //根据物料编码,可用 和数量查找对应资产.  首先找费用仓 1
            List<Assets> list1 = assetsDao.findUnuseByItemid(itemid,1);
            if (list1.size()>=count){  //费用仓够
                for (int c = 0;c<count;c++){
                    if (list1.get(c)!=null){
                        out.add(list1.get(c));
                    }else {
                        System.out.println("出现错误! 资产为null");
                    }
                }
            }
            else {  //费用仓不够
                List<Assets> list2 = assetsDao.findUnuseByItemid(itemid,0);
                if (count-list1.size()-list2.size()>0){ //资产仓还是不够
                    result.put("err",null);
                    return result;
                }
                count = count - list1.size();
                if (list1.size()>0){
                    for (Assets a : list1){
                        if (a!=null){
                            out.add(a);
                        }else{
                            System.out.println("出现错误! 资产为null");
                        }
                    }
                }
                for (int d = 0;d<count;d++){
                    if (list2.get(d)!=null){
                        out.add(list2.get(d));
                    }else {
                        System.out.println("出现错误! 资产为null");
                    }
                }
            }


        }
        result.put("ok",out);
        return result;
    }

    /**
     * 提单事件公用方法
     * @param index
     * @param event
     * @param request
     * @return
     */
    public String subBill(int index, int event, HttpServletRequest request){
        // yyyyMMddenvent-01 年月日event一样说明是同一事件,流水号说明该事件第几个物料
        String userid = request.getParameter("userid");
        if (StringUtils.isEmpty(userid)){
            return "EmptyUserid";
        }
        //String name = request.getParameter("name").trim();
        String remark = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+event;
        // List<Event> list = new ArrayList<>();
        /**
         * 前端固定最多20个 , 循环20次的原因是, 不确定用户删除某计列后, 命名不是按照顺序的
         *  例如 type1 type3 type5 用index 数量去循环 就只能得到 type1 tpye2 type3.
         */
        int eventTime = 1;
        for (int i = 1; i<=20; i++){
            System.out.println(i);
            String type = request.getParameter("type"+i);
            System.out.println(type);
            if (StringUtils.isEmpty(type)){
                continue;
            }
            String uuid = uuid1;
            uuid = uuid+"-"+eventTime;
            eventTime ++;
            String assetsid = request.getParameter("assetsid"+i);
            String brand = request.getParameter("brand"+i);
            int count = Integer.valueOf(request.getParameter("count"+i));
            String unit = request.getParameter("unit"+i);
            //     list.addItemController(e);
            //根据type , brand 去找物料ID , 根据count 去Assets资产表中更改字段,
            // 如果有assetsid,还要在出入的资产中添加进去.
            // 然后再去Access出入表中记录
            //查询物料ID
            Item item =  itemDao.findByTypeAndBrand(type,brand);
            //获取操作人员
            HttpSession session = request.getSession();
            String adminuser = (String)session.getAttribute("username");
            Event e = new Event(uuid,event,item.getItemid(),count,unit,userid,adminuser,date,remark);
            System.out.println("e:"+e);
            //所有准备出的资产
            List<Assets> out = new ArrayList<>(count);
            //如果有assetsid , 首先找该资产
            if (!StringUtils.isEmpty(assetsid)){
                Assets a = assetsDao.findByAssetsid(assetsid);
                if (a!=null){
                    if (a.getState()==1||a.getState()==2){
                        return assetsid+"资产不在仓库!";
                    }
                    out.add(a);
                    count--;
                }
            }
            //需求数量还不为0
            if (count>0){
                Map<String,List<Assets>> res = outList(count,item.getItemid());
                if (res.get("ok")==null){
                    //回滚事务
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return type+"数量不足!";
                }
                List<Assets> tmp = res.get("ok");
                out.addAll(tmp);
            }

            //保存事件
            eventDao.save(e);
            //更改出库的状态 , 并添加出入记录
            if (out.size()>0){
                for (int z=0;z<out.size();z++){
                    Assets a = out.get(z);
                    //如果assetsid存在且不在库中,说明是出库时贴的资产牌, 在out list中,随便扣一个贴上
                    if (!StringUtils.isEmpty(assetsid)){
                        //assetsid放入第一个同类资产
                        if (z==0){
                            a.setAssetsid(assetsid);
                        }
                    }
                    a.setUserid(userid);
                    a.setStore(1);  //设为费用仓
                    a.setState(1);  //设为出库状态
                    assetsDao.save(a);
                    Access access = new Access();
                    access.setAid(a.getId());
                    access.setEventid(uuid);
                    accessDao.save(access);
                }
            }


        }
        return "ok";
    }
}
