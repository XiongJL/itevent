package com.liwinon.itevent.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventStepDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.dao.primaryRepo.RepairUserDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventStep;
import com.liwinon.itevent.entity.primary.RepairUser;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.util.UpdateImgUtil;

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
	@Autowired
	RepairUserDao repairUserDao;
	 
	
	
	@SuppressWarnings("null")
	@Override
	@Transactional
	public JSONObject initiationpc(HttpServletRequest request, MultipartFile[] files,
			String userid, String phone,String adminuser,
			String level_1, String level_2, String description, 
			String type, String brand, String itemid,
			String remark) {
		UpdateImgUtil updateImgUtil=new UpdateImgUtil();
		String path=updateImgUtil.updateImg(files);
		JSONObject json=new JSONObject();
		if(path==null) {
			json.accumulate("code",400);
	        json.accumulate("msg","图片存储失败，请重新填写事件，上传图片");
	        json.accumulate("data","no1");
			return  json;
		}else if("文件过大,内存溢出异常".equals(path)||"文件路径错误,IO异常".equals(path)){
			json.accumulate("code",400);
	        json.accumulate("msg",path);
	        json.accumulate("data","no1");
			return  json;
		}
		EventStep eventStep=new EventStep();
		Event event=new Event();
		Integer etypeid=eventTypeDao.findAllBylevel_2(level_2);
		//生成it事件id
		Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+etypeid;
        String uuid = uuid1+"-"+1;
        eventStep.setUuid(uuid);
		eventStep.setStep(1);
		eventStep.setImgurl(path);
		eventStepDao.save(eventStep);
		event.setUuid(uuid);
		event.setEvent(etypeid);
		event.setUserid(userid);
		event.setPhone(phone);
		 //获取操作人员
        HttpSession session = request.getSession();
        if(adminuser!=null&&!"".equals(adminuser)) {
        	event.setAdminuser(adminuser);        	
        }else {
        	 String adminusera = (String)session.getAttribute("username");
        	 event.setAdminuser(adminuser);        	
        }
		event.setDate(new Date());
		event.setRemark(remark);
		event.setState("受理中");
		event.setItemid(itemid);
		eventDao.save(event);
		
		json.accumulate("code",200);
        json.accumulate("msg","记录成功");
        json.accumulate("data","ok");
        String team =eventTypeDao.findallTeam(description);
        String[] user ;
        if("erp".equals(team)) {
        	int min=repairUserDao.findAllmin(team);
        	user =repairUserDao.findAllUseridrep(min,team);
        }else {
        	int min=repairUserDao.findAllmin(team);
        	user =repairUserDao.findAllUserid(min,team);
        }
       /* https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?
        * access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD
        * ACCESS_TOKEN=wwbc7acf1bd2c6f766
        * department_id=2
        * fetch_child =     是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门*/        
        wxApi.sendMissionToIT(user,level_1+"的服务申请", "  类型: "+level_2+"<br> 申请描述: "+description+"<br>" + " 事件等级: 一般",
        		uuid,
        		new String[]{"1","2"},new String[]{"拒接","接收"},new String[]{"已拒接","开始处理"},"");
		return json;
	}
	
	
	@SuppressWarnings("null")
	@Override
	@Transactional
	public JSONObject initiationmobile(HttpServletRequest request, List<MultipartFile> files,
			String userid, String phone,String adminuser,
			String level_1, String level_2, String description, 
			String type, String brand, String itemid,
			String remark) {
		UpdateImgUtil updateImgUtil=new UpdateImgUtil();
		String path=updateImgUtil.updateImg(files);
		JSONObject json=new JSONObject();
		if(path==null) {
			json.accumulate("code",400);
	        json.accumulate("msg","图片存储失败，请重新填写事件，上传图片");
	        json.accumulate("data","no1");
			return  json;
		}else if("文件过大,内存溢出异常".equals(path)||"文件路径错误,IO异常".equals(path)){
			json.accumulate("code",400);
	        json.accumulate("msg",path);
	        json.accumulate("data","no1");
			return  json;
		}
		EventStep eventStep=new EventStep();
		Event event=new Event();
		Integer etypeid=eventTypeDao.findAllBylevel_2(level_2);
		//生成it事件id
		Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid1 = sdf.format(date)+"-"+etypeid;
        String uuid = uuid1+"-"+1;
        eventStep.setUuid(uuid);
		eventStep.setStep(1);
		eventStep.setImgurl(path);
		eventStepDao.save(eventStep);
		event.setUuid(uuid);
		event.setEvent(etypeid);
		event.setUserid(userid);
		event.setPhone(phone);
		 //获取操作人员
        HttpSession session = request.getSession();
        if(adminuser!=null&&!"".equals(adminuser)) {
        	event.setAdminuser(adminuser);        	
        }else {
        	 String adminusera = (String)session.getAttribute("username");
        	 event.setAdminuser(adminuser);        	
        }
		event.setDate(new Date());
		event.setRemark(remark);
		event.setState("受理中");
		event.setItemid(itemid);
		eventDao.save(event);
		
		json.accumulate("code",200);
        json.accumulate("msg","记录成功");
        json.accumulate("data","ok");
        
        String team =eventTypeDao.findallTeam(description);
        String[] user ;
        if("erp".equals(team)) {
        	int min=repairUserDao.findAllmin(team);
        	user =repairUserDao.findAllUseridrep(min,team);
        }else {
        	int min=repairUserDao.findAllmin(team);
        	user =repairUserDao.findAllUserid(min,team);
        }
       /* https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?
        * access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD
        * ACCESS_TOKEN=wwbc7acf1bd2c6f766
        * department_id=2
        * fetch_child =     是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门*/        
        wxApi.sendMissionToIT(user,level_1+"的服务申请", "&nbsp; 类型: "+level_2+"<br> 申请描述: "+description+"<br>" + " 事件等级: 一般",
        		uuid,
        		new String[]{"1","2"},new String[]{"拒接","接收"},new String[]{"已拒接","开始处理"},"");
		return json;
	}
}

