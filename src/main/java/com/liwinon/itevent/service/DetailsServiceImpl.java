package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.*;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.Model.DetailsModel;
import com.liwinon.itevent.entity.Model.MissionModel;
import com.liwinon.itevent.entity.Model.MissionStepModel;
import com.liwinon.itevent.entity.Model.QEventModel;
import com.liwinon.itevent.entity.StepEnum;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.entity.primary.RepairUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetailsServiceImpl implements DetailsService {
    @Autowired
    EventStepDao eventStepDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    EventTypeDao eventTypeDao;
    @Autowired
    SapDao sapDao;
    @Autowired
    RepairUserDao repairUserDao;
    

    @SuppressWarnings("null")
	@Override
    public Map<String,Object> queryDetails(String qywxid) {
    	 Map<String,Object> res = new HashMap<>();
          RepairUser repairUser=  repairUserDao.findByUserid(qywxid);    // 得到处理人信息   
		   List<EventStep> listEventStep= eventStepDao.findByqywxid(qywxid);  //处理人对应的正在处理中的事件
		   Event events = new Event();
		   EventStep eventStep = new EventStep();
		   List<DetailsModel> model = new ArrayList<>();
		   for (int i = 0; i < listEventStep.size(); i++) {
			   events=eventDao.findByUuid(listEventStep.get(i).getUuid());   //得到event详细信息
			   eventStep= listEventStep.get(i); //得到图片信息
			   eventStep.setImgurl(eventStepDao.findByUuidAndStep1(listEventStep.get(i).getUuid()).getImgurl());
			   EventType type = eventTypeDao.findByETypeId(events.getEvent());
			    int  aa = eventStep.getStep();  //1受理中,2-20 处理中,30回访-满意度调查,40结束}   ,  或者 关键节点 为负数 -1 -2
			    String stepStr;
	            if(aa<0){
	                stepStr ="处理中";
	            }else if(aa==1){
	            	stepStr ="处理中";	            	
	            }else if(aa==30) {
	            	stepStr ="回访";	            	
	            }else {
	            	stepStr ="处理中";	            	
	            }
	            String[] imgUrls =null;
	            String aaaaa = null;
	            if (!StringUtils.isEmpty(eventStep.getImgurl())){
	                String[] tmpUrls = eventStep.getImgurl().split(",");
	                if (tmpUrls.length>0){
	                    imgUrls =  new String[tmpUrls.length];
	                    for (int a=0;a<tmpUrls.length;a++){
	                        imgUrls[a]  = tmpUrls[a];
	                        aaaaa=tmpUrls[a];
	                    }
	                }
	            }else{
	                imgUrls = new String[]{};
	            }
	            String aaa=sapDao.findNByUserId(events.getUserid());
			   model.add(new DetailsModel(events.getUuid(),events.getDate(),aaa,events.getUserid(),events.getPhone(),
					   stepStr,type.getLevel_2(),type.getDescription(),events.getItemid(),
					   events.getRemark(),events.getAdminuser(),imgUrls));
			   /* EventStep eventStep=listEventStep.get(i);
			   Event event = eventDao.findAllUuid(eventStep.getUuid());
			   EventType eventType=eventTypeDao.findAllEtypeide(event.getEvent());
			   String title = eventType.getLevel_1()+"的服务处理信息";
			   String description ="申请人员:"+sapDao.findNByUserId(event.getUserid())+"<br>"+ "申请类型:"+eventType.getLevel_2()+"<br>"+"申请描述:"+event.getRemark();
			   String btntxt = "查看详细信息";
			   String URL = WxConfig.DetailsURL.getValue()+eventStep.getUuid()+"&qyid="+event.getQyid()+"&userid="+event.getUserid();
			   wxApi.sendCardToIT(new String[]{event.getQyid()},title,description,URL,btntxt);*/
		   }
		   res.put("data", model);
		   res.put("personid", repairUser.getPersonid());
        return res;
    }
   
    
    
}
