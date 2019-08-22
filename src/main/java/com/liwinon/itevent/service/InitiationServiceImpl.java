package com.liwinon.itevent.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.liwinon.itevent.service.InitiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventStepDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.qywx.WxApi;

import net.sf.json.JSONObject;
@Service
public class InitiationServiceImpl implements InitiationService {
	
	@Autowired
	EventTypeDao eventTypeDao;
	
	@Override
	public JSONObject level_1() {
		JSONObject json=new JSONObject();
		 List  ev=eventTypeDao.findAlllevel_1();
		 json.accumulate("code",200);
         json.accumulate("msg","无数据");
         json.accumulate("data",ev);
 		return json;
	}

	@Override
	public JSONObject level_2(HttpServletRequest request) {
		JSONObject json=new JSONObject();
		String value=request.getParameter("value");
		 List  ev=eventTypeDao.findAlllevel_2(value);
		 json.accumulate("code",200);
        json.accumulate("msg","无数据");
        json.accumulate("data",ev);
		return json;
	}

	@Override
	public JSONObject description(HttpServletRequest request) {
		JSONObject json=new JSONObject();
		String value=request.getParameter("value");
		String  ev=eventTypeDao.findAllldescription(value);
		json.accumulate("code",200);
        json.accumulate("msg","无数据");
        json.accumulate("data",ev);
		return json;
	}
	
	@Autowired
	EventDao eventDao;
	@Autowired
	EventStepDao eventStepDao;
	@Autowired
	WxApi wxApi;
	 
	@Override
	@Transactional
	public JSONObject postinitiation(HttpServletRequest request) {
		JSONObject json=new JSONObject();
		EventStep eventStep=new EventStep();
		Event event=new Event();
		String level_1=request.getParameter("level_1");
		String level_2=request.getParameter("level_2");
		String description=request.getParameter("description");
		Integer etypeid=eventTypeDao.findAllBylevel_2(level_2);
		
		//生成it事件id
		Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+etypeid;
        String uuid = uuid1+"-"+1;
        eventStep.setUuid(uuid);
		eventStep.setStep(1);
		eventStep.setStepDate(new Date());
		eventStepDao.save(eventStep);
		
		
		event.setUuid(uuid);
		event.setEvent(etypeid);
		String userid=request.getParameter("userid");
		event.setUserid(userid);
		String phone=request.getParameter("phone");
		event.setPhone(phone);
		 //获取操作人员
        HttpSession session = request.getSession();
        String adminuser = (String)session.getAttribute("username");
		event.setAdminuser(adminuser);
		event.setDate(new Date());
		String remark=request.getParameter("remark");
		event.setRemark(remark);
		event.setState("受理中");
		String itemid=request.getParameter("itemid");
		event.setItemid(itemid);
		eventDao.save(event);
		
		json.accumulate("code",200);
        json.accumulate("msg","记录成功");
        json.accumulate("data","ok");
      /*  wxApi.sendMissionToIT(new String[]{userid,executorid},level_1+"的服务申请",
                " 申请类型: 软件无法正常工作<br> 申请描述: 用户填写的内容<br>" +
                        " 事件等级: 加急处理",
                "6",
                new String[]{"1","2"},new String[]{"拒接","接收"},new String[]{"已拒接","开始处理"},"http://www.baidu.com");
        */
        
		return json;
	}

}
