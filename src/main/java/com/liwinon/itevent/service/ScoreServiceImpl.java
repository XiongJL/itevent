package com.liwinon.itevent.service;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventStepDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.dao.primaryRepo.ScoreDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.entity.primary.Score;
import com.liwinon.itevent.qywx.WxApi;

import net.sf.json.JSONObject;

@Service
public class ScoreServiceImpl implements ScoreService{
	
	@Autowired
	ScoreDao scoreDao;
	@Autowired
	EventStepDao eventStepDao;
	@Autowired
	EventDao eventDao;
	@Autowired
	WxApi wxApi;
	@Autowired
	EventTypeDao eventTypeDao;
	
	@Override
	@Transactional
	public JSONObject Save(String qyid, String uuid, String score, String remark, HttpServletRequest request) {
		JSONObject json=new JSONObject();
		String uu=null;
		 uu=scoreDao.findByUuid(uuid);
		if(uu!=null) {
			json.accumulate("code",400);
			json.accumulate("msg","请勿重复填写评论");
			json.accumulate("data","no");
			return json;
		}
		Score score1=new Score();
		score1.setUuid(uuid);
		score1.setQyid(qyid);
		score1.setScore(score);
		score1.setRemark(remark);
		try {
			EventStep eventStep=eventStepDao.findAlluuidAndstep(uuid);
			eventStep.setStep(40);  //事件结束标记
			eventStepDao.save(eventStep);
			Event event=eventDao.findAllUuid(uuid);
			score1.setPhone(event.getPhone());
			score1.setUserid(event.getUserid());
			event.setState("结束");  //结束标记
			eventDao.save(event);
			System.out.println(score.toString());
			scoreDao.save(score1);
			EventType eventType=eventTypeDao.findAllEtypeide(event.getEvent()); 
			wxApi.sendTextToOne(new String[]{eventStep.getExecutorId()},"您处理的任务"+eventType.getLevel_2()+"已完成回访，谢谢");
			json.accumulate("code",200);
	        json.accumulate("msg","评价完成");
	        json.accumulate("data","ok");
			return json;
		} catch (Exception e) {
			json.accumulate("code",400);
			json.accumulate("msg","发生错误请重新填写");
			json.accumulate("data","no");
			return json;
		}
	}

}
