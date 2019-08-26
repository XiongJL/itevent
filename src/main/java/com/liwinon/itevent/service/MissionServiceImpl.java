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
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //由于根据降序排序,第一个则是最后一次更新的步骤
        EventStep last = eSteps.get(eSteps.size()-1);
        JSONObject json = new JSONObject();
        try {
            //比对用户是否匹配
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            String personid =  adminDao.findByUser(username).getUserid();
            RepairUser me = repairUserDao.findByPersonid(personid);
            String userid = me.getUserid();
            if (!qyid.equals(userid)){  //如果和访问的用户不匹配, 不能发起转移
                json.accumulate("code","notMatch");
                json.accumulate("data","企业微信账号和当前节点执行人不匹配");
                return json;
            }
            //匹配成功,根据事件类型的组别和等级查找可转移对象,
            int myLevel = me.getUserlevel();
            List<RepairUser> canMoveUsers = null;
            if (myLevel==1){
                //查找1-2级同组的
                canMoveUsers = repairUserDao.findByUserlevelAndTeam(myLevel,me.getTeam());
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
    public List<MissionStepModel> TransferToStepModel(List<EventStep> eventSteps){
        if (eventSteps.size()<=0)
            return null;
        List<MissionStepModel> list = new ArrayList<>();
        for (EventStep eventStep : eventSteps){
            if (eventStep==null)
                continue;
            MissionStepModel model = new MissionStepModel(eventStep.getStepId(),eventStep.getUuid()
                    , StepEnum.getStep(eventStep.getStep()),eventStep.getExecutorId(),eventStep.getExecutorName()
                    ,eventStep.getStepDate(),eventStep.getImgurl());
            list.add(model);
        }
        return  list;

    }
}
