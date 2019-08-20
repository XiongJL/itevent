package com.liwinon.itevent.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;

import net.sf.json.JSONObject;

public interface InitiationService {

	JSONObject level_1();

	JSONObject level_2(HttpServletRequest request);

	JSONObject description(HttpServletRequest request);

	JSONObject postinitiation(HttpServletRequest request);

}
