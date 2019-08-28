package com.liwinon.itevent.thetimer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.primaryRepo.EventTypeDao;
import com.liwinon.itevent.dao.primaryRepo.RepairUserDao;
import com.liwinon.itevent.entity.primary.Event;
import com.liwinon.itevent.entity.primary.EventType;
import com.liwinon.itevent.qywx.WxApi;

@Component
public class InitiationTheTimer {
	
	@Autowired
	EventDao eventDao;
	@Autowired
	EventTypeDao eventTypeDao;
	@Autowired
	RepairUserDao repairUserDao;
	@Autowired
	WxApi wxApi;
	
	/**总共七位，分别表示秒（0-59），分（0-59），时（0-23），
	 * 日期天/日（1-31），月份）（1-12），星期（1-7,1表示星晴天，7表示星期六），
	 * 年（可以缺省。取值范围是1970-2099）。*/
	@Scheduled(cron="0 0/1 * * * ?")
	public void  taskStatus() {
		//1  查询event 表中 state字段事件状态为 受理中 的数据
		List<Event> event=eventDao.findAllState();
		//获取一个标准的当前时间戳
		long data=new Date().getTime();
		halfAnHour(event,data);
		hours(event,data);
		twoHours(event,data);
		
	}
	
	//半小时的推送   2级
	public void halfAnHour(List<Event> event,long data) {
		//获取当前时间的时间戳
		long a=data;
		//获取30分钟前的时间戳
		long b=a-30*60*1000;
		//获取31分钟前的时间戳 ，后面多减的时间戳10为补偿时间，，创建标准时间戳的补偿
		long c=data-31*60*1000-10;     
		for(int i=0;i<event.size();i++){
			Event event2=event.get(i);
			//时间戳比较，在时间戳范围之类的就推送消息
			if(c<event2.getDate().getTime()&&event2.getDate().getTime()<b) {
				int etypeid=event2.getEvent();
				EventType eventType=eventTypeDao.findAllEtypeide(etypeid);
				if("erp".equals(eventType.getTeam())) {  //erp推送给最小级别人员
					int min=repairUserDao.findAllmin(eventType.getTeam());
					String[] user=repairUserDao.findAllUserid(min,eventType.getTeam());
					wxSenfMissionToIT(user,eventType.getLevel_1(),eventType.getLevel_2(),eventType.getDescription(),tookenid(event2.getUuid())+"2");					
				}else {
					int min=repairUserDao.findAllmin(eventType.getTeam());
					String[] user=repairUserDao.findAllUserid(min+1,eventType.getTeam());
					wxSenfMissionToIT(user,eventType.getLevel_1(),eventType.getLevel_2(),eventType.getDescription(),tookenid(event2.getUuid())+"2");
				}
			}
		}
	}
	
	//1个小时的推送
	public void hours(List<Event> event,long data) {
		//获取当前时间的时间戳
		long a=data;
		//获取60分钟前的时间戳
		long b=a-60*60*1000;
		//获取61分钟前的时间戳 ，后面多减的时间戳10为补偿时间，，创建标准时间戳的补偿
		long c=data-61*60*1000-10;     
		for(int i=0;i<event.size();i++){
			Event event2=event.get(i);
			//时间戳比较，在时间戳范围之类的就推送消息
			if(c<event2.getDate().getTime()&&event2.getDate().getTime()<b) {
				int etypeid=event2.getEvent();
				EventType eventType=eventTypeDao.findAllEtypeide(etypeid);
				if("erp".equals(eventType.getTeam())) {  //erp推送给最小级别人员
					int min=repairUserDao.findAllmin(eventType.getTeam());
					String[] user=repairUserDao.findAllUserid(min+1,eventType.getTeam());
					wxSenfMissionToIT(user,eventType.getLevel_1(),eventType.getLevel_2(),eventType.getDescription(),tookenid(event2.getUuid())+"3");					
				}else {
					int min=repairUserDao.findAllmin(eventType.getTeam());
					String[] user=repairUserDao.findAllUserid(min+2,eventType.getTeam());
					wxSenfMissionToIT(user,eventType.getLevel_1(),eventType.getLevel_2(),eventType.getDescription(),tookenid(event2.getUuid())+"3");
				}
			}
		}
	} 
	
	//2个小时的推送  
	public void twoHours(List<Event> event,long data) {
		//获取当前时间的时间戳
		long a=data;
		//获取120分钟前的时间戳
		long b=a-120*60*1000;
		//获取121分钟前的时间戳 ，后面多减的时间戳10为补偿时间，创建标准时间戳的补偿
		long c=data-121*60*1000-10;     
		for(int i=0;i<event.size();i++){
			Event event2=event.get(i);
			//时间戳比较，在时间戳范围之类的就推送消息
			if(c<event2.getDate().getTime()&&event2.getDate().getTime()<b) {
				int etypeid=event2.getEvent();
				EventType eventType=eventTypeDao.findAllEtypeide(etypeid);
			    String[] user=repairUserDao.findAllUserid(4,"经理");
			    wxSenfMissionToIT(user,eventType.getLevel_1(),eventType.getLevel_2(),eventType.getDescription(),tookenid(event2.getUuid())+"4");
			}
		}
	}
	public String tookenid(String var) {
        String[] str =  var.split("-");
        String task_id = "";
        for (int i= 0;i<str.length-1;i++){ //拼接最后一个流水号, 防止发送的uuid不匹配.
            task_id +=str[i] +"-";
        }
        return task_id;
	}
	//公用推送方法
	public  void wxSenfMissionToIT(String[] user,String levent_1,String levent_2,String description,String uuid) {
		 wxApi.sendMissionToIT(user,levent_1+"的服务申请",
	    		"&nbsp;申请类型: "+levent_2+"<br> 申请描述: "+
				description+"<br>" + " 事件等级: 一般",
				uuid,
        		new String[]{"1","2"},new String[]{"拒接","接收"},new String[]{"已拒接","开始处理"},"");
	}
}	
