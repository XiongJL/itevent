package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventStepDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.Model.MissionModel;
import com.liwinon.itevent.entity.Model.MissionStepModel;
import com.liwinon.itevent.entity.StepEnum;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public MissionModel TransferToMission(Event event){
        if (event==null)
            return null;
        MissionModel model = new MissionModel(event.getUuid(),
                eventTypeDao.findByETypeId(event.getEvent()).getLevel_2() ,
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
