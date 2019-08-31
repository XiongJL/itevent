package com.liwinon.itevent.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface ScoreService {

	JSONObject Save(String qyid, String uuid,String score,String remark, HttpServletRequest request);

}
