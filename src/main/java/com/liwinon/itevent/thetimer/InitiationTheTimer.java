package com.liwinon.itevent.thetimer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.entity.primary.Event;

@Component
public class InitiationTheTimer {
	
	@Autowired
	EventDao eventDao;
	
	/**总共七位，分别表示秒（0-59），分（0-59），时（0-23），
	 * 日期天/日（1-31），月份）（1-12），星期（1-7,1表示星晴天，7表示星期六），
	 * 年（可以缺省。取值范围是1970-2099）。*/
	@Scheduled(cron="0 0/1 * * * ?")
	public void  taskStatus() {
		//1  查询event 表中 state字段事件状态为 受理中 的数据
		List<Event> evtnt=eventDao.findAllState();
		
		
	}
}	
