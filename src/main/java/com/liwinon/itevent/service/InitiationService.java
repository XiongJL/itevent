package com.liwinon.itevent.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

public interface InitiationService {

	JSONObject level_1();

	JSONObject level_2(HttpServletRequest request);

	JSONObject description(HttpServletRequest request);

	JSONObject initiationpc(HttpServletRequest request, MultipartFile[] files, String userid, String phone, String adminuser,String level_1, String level_2, String description, String type, String brand, String itemid, String remark);
	JSONObject initiationmobile(HttpServletRequest request, List<MultipartFile> files, String userid, String phone, String adminuser,String level_1, String level_2, String description, String type, String brand, String itemid, String remark);

}
