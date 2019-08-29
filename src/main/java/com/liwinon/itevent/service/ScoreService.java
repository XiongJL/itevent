package com.liwinon.itevent.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface ScoreService {

	JSONObject Save(String uuid, String qyid,String phone,String userid,String score,String remark, HttpServletRequest request);

}
