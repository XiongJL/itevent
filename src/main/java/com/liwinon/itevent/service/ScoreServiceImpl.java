package com.liwinon.itevent.service;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liwinon.itevent.dao.primaryRepo.ScoreDao;
import com.liwinon.itevent.entity.primary.Score;

import net.sf.json.JSONObject;

@Service
public class ScoreServiceImpl implements ScoreService{
	
	@Autowired
	ScoreDao scoreDao;
	
	@Override
	@Transactional
	public JSONObject Save(String uuid, String qyid, String phone,String userid, String score, String remark, HttpServletRequest request) {
		JSONObject json=new JSONObject();
		Score score1=new Score();
		score1.setUuid(uuid);
		score1.setQyid(qyid);
		score1.setPhone(phone);
		score1.setUserid(userid);
		score1.setScore(score);
		score1.setRemark(remark);
		try {
			scoreDao.save(score1);
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
