package com.liwinon.itevent.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventStepDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;

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
        String step=request.getParameter("step");
		eventStep.setStep(step);
		String executorid=request.getParameter("executorid");
		eventStep.setExecutorId(executorid);
		String executorname=request.getParameter("executorname");
		eventStep.setExecutorName(executorname);
		String stepdate=request.getParameter("stepdate");
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date datea = fmt.parse(stepdate.trim());
			eventStep.setStepDate(datea);
		} catch (ParseException e) {
			json.accumulate("code",400);
	        json.accumulate("msg","请按标准选择时间");
	        json.accumulate("data","no");
			return json;
		}
		eventStepDao.save(eventStep);
		
		
		event.setUuid(uuid);
		event.setEvent(etypeid);
		
		String userid=request.getParameter("userid");
		event.setUserid(userid);
		String phone=request.getParameter("phone");
		event.setPhone(phone);
		String adminuser=request.getParameter("adminuser");
		event.setAdminuser(adminuser);
		String datea=request.getParameter("date");
		try {
			Date datea1 = fmt.parse(datea.trim());
			event.setDate(datea1);
		} catch (ParseException e) {
			json.accumulate("code",400);
			json.accumulate("msg","请按标准选择时间");
	        json.accumulate("data","no");
			return json;
		}
		String remark=request.getParameter("remark");
		event.setRemark(remark);
		event.setState("受理中");
		String itemid=request.getParameter("itemid");
		event.setItemid(itemid);
		eventDao.save(event);
		
		json.accumulate("code",200);
        json.accumulate("msg","记录成功");
        json.accumulate("data","ok");
		return json;
	}

}
