package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.*;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.Model.MissionModel;
import com.liwinon.itevent.entity.Model.MissionStepModel;
import com.liwinon.itevent.entity.StepEnum;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.entity.primary.RepairUser;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.qywx.WxConfig;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class MissionServiceImpl implements MissionService {
    @Autowired
    EventStepDao eventStepDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    EventTypeDao eventTypeDao;
    @Autowired
    SapDao sapDao;
    @Autowired
    AdminDao adminDao;
    @Autowired
    RepairUserDao repairUserDao;
    @Autowired
    WxApi wxApi;


    @Override
    public Map<String,Object> queryMission(String uuid) {
        Event e = eventDao.findByUuid(uuid);
        if (e==null)
            return null;
        Map<String,Object> res = new HashMap<>();
        List<EventStep> eSteps = eventStepDao.findByUuid(uuid);
        res.put("start",TransferToMission(e));
        List<MissionStepModel> ings =  TransferToStepModel(eSteps);
        if (ings==null){
            return null;
        }
        res.put("ing",ings);
        String[] imgUrls =null;
        if (eSteps.size()>0){
            EventStep tmp = eSteps.get(eSteps.size()-1);  //获取环节最开始的图片信息,倒叙
            if (!StringUtils.isEmpty(tmp.getImgurl())){
                String[] tmpUrls = tmp.getImgurl().split(",");
                if (tmpUrls.length>0){
                    imgUrls =  new String[tmpUrls.length];
                    for (int i=0;i<tmpUrls.length;i++){
                        imgUrls[i]  = tmpUrls[i];
                    }
                }
            }else{
                imgUrls = new String[]{};
            }
        }else{
            imgUrls = new String[]{};
        }
        res.put("imgUrls",imgUrls);
        res.put("imgLength",imgUrls.length);

        return res;
    }

    /**
     * 查询在当前时间段内可以转交的人员
     * @param uuid
     * @param qyid
     * @param request
     * @return
     */
    @Override
    public JSONObject CanTurnOverUsers(String uuid, String qyid, HttpServletRequest request) {
        List<EventStep> eSteps = eventStepDao.findByUuid(uuid);
        //由于DAO根据降序排序,第一个是最新的步骤
        EventStep last = eSteps.get(0);
        JSONObject json = new JSONObject();
        try {
            //比对用户是否匹配
            HttpSession session = request.getSession();
            //判断操作的方式
            String access = (String)session.getAttribute("AccessMode");
            String username = (String) session.getAttribute("username");
            String personid =  adminDao.findByUser(username).getUserid();
            RepairUser me = repairUserDao.findByPersonid(personid);
            //匹配成功,根据事件类型的组别和等级查找可转移对象,
            int myLevel = me.getUserlevel();
            List<RepairUser> canMoveUsers = null;
            if (myLevel==1){
                //查找1-2级同组的
                canMoveUsers = repairUserDao.findByLevel1And2AndTeam(me.getTeam());
            }else{
                //除了1级, 2,3可以转移给 1,2,3级的同组
                canMoveUsers = repairUserDao.findByTeam(me.getTeam());
            }
            json.accumulate("code","ok");
            json.accumulate("data",canMoveUsers);
        }catch (NullPointerException e){
            e.printStackTrace();
            JSONObject exception = new JSONObject();
            exception.accumulate("code","exception");
            exception.accumulate("data","有值为Null");
            return exception;
        }

        return json;
    }




    /**
     * 移交任务给某人
     * @param toPersonid  将移交的对象
     * @param fromPersonid  操作人
     * @param uuid  事件号
     * @return
     */
    @Transactional
    public JSONObject changeUser(String toPersonid, String fromPersonid, String uuid,String fromQyid,HttpServletRequest request) {
        List<EventStep> eSteps = eventStepDao.findByUuid(uuid);
        JSONObject json = new JSONObject();
        EventStep last = eSteps.get(0);
        int maxStep = eSteps.get(0).getStep();
        //比对用户是否匹配
        HttpSession session = request.getSession();
        //判断操作的方式
        String access = (String)session.getAttribute("AccessMode");
        RepairUser me = repairUserDao.findByPersonid(fromPersonid);
        RepairUser to = repairUserDao.findByPersonid(toPersonid);

        if (me.getUserlevel()<=to.getUserlevel()){ //如果是上级转移给下级, 不需要判断当前节点的执行人
            if ("wx".equals(access) || "qywx".equals(access)){
                String userid = me.getUserid();
                if (!fromQyid.equals(userid)||last.getExecutorId()==null|| //判断微信号
                        !last.getExecutorId().equals(fromPersonid)){  //判断操作人员工号
                    json.accumulate("code","notMatch");
                    //级别不够
                    json.accumulate("msg","企业微信账号或工号和当前节点执行人不匹配,无法移交");
                    return json;
                }
            }else{ // 浏览器端.
                if (last.getExecutorId()==null || !last.getExecutorId().equals(fromPersonid)){ //只判断工号
                    json.accumulate("code","notMatch");
                    json.accumulate("msg","工号和当前节点执行人不匹配,无法移交");
                    return json;
                }
            }
        }

        if ( maxStep>= 29  ){
            json.accumulate("code","fail");
            json.accumulate("msg","当前环节不能移交");
            return json;
        }else{
            EventStep newStep = new EventStep();
            newStep.setStepDate(new Date());
            newStep.setStep(maxStep+1);
            newStep.setExecutorId(toPersonid);
            newStep.setExecutorName(repairUserDao.findByPersonid(toPersonid).getName());
            newStep.setUuid(uuid);
            eventStepDao.save(newStep);  //保存新节点
            //推送给被移交人.
            wxApi.sendCardToIT(new String[]{to.getUserid()},"您收到一条任务移交",
                    repairUserDao.findByPersonid(fromPersonid).getName()+
                            "移交给你一条任务,请尽快查看任务.", WxConfig.QMissionURL.getValue()+uuid+"&qyid="+to.getUserid(),"查看详情");
            json.accumulate("code","ok");
            json.accumulate("msg","移交成功");
            return json;
        }

    }


    public MissionModel TransferToMission(Event event){
        if (event==null)
            return null;
        EventType type = eventTypeDao.findByETypeId(event.getEvent());
        MissionModel model = new MissionModel(event.getUuid(),
                type.getLevel_2() ,type.getDescription(),
                event.getUserid(),
                sapDao.findNByUserId(event.getUserid()),
                event.getPhone(),event.getItemid(),event.getDate(),event.getAdminuser(),
                sapDao.findNByUserId(event.getAdminuser()),event.getState(),event.getRemark());
        return model;
    }

    /**
     *
     * 转换为前端倒叙显示的步骤.
     * 1受理中,2-20 处理环节,30结束,40回访-满意度调查,50竣工
     * 数据库返回的值是 n n-1... 3 2 1
     * 前端显示为  n n-1 ... 1
     * @param eventSteps
     * @return
     */
    public List<MissionStepModel> TransferToStepModel(List<EventStep> eventSteps){
        if (eventSteps.size()<=0)
            return null;
        List<MissionStepModel> list = new ArrayList<>();
        String stepStr = "";
        for (EventStep eventStep : eventSteps){
            if (eventStep==null)
                continue;
            stepStr = StepEnum.getStep(eventStep.getStep());
            if(stepStr==null || stepStr ==""){
                stepStr ="处理中";
            }
            MissionStepModel model = new MissionStepModel(eventStep.getStepId(),eventStep.getUuid()
                    , stepStr,eventStep.getExecutorId(),eventStep.getExecutorName()
                    ,eventStep.getStepDate(),eventStep.getImgurl());
            list.add(model);
        }
        return  list;

    }

	@Override
	public JSONObject complete(String fromPersonid, String uuid, String qyid, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		
		
		
		
		return json;
	}
}
