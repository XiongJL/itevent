package com.liwinon.itevent.dao;

import com.liwinon.itevent.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventDao extends JpaRepository<Event,String>, JpaSpecificationExecutor<Event> {
    //查询7天内即将逾期的且是在外借用状态的事件 , 但是不能知道具体的型号,有可能归还了某几个
    //需要通过事件号再去出入表具体查询剩余归还数量

    /**
     * select DISTINCT i.* from ITE_Event i,ITE_Assets a where DateDiff(dd,date,getdate())<=30 and  DateDiff(dd,date,getdate())>=23 and event = 5
     * and a.itemid = i.itemid
     * and a.state = 2
     * @return
     */
    @Query(value = "select DISTINCT i.* from ITE_Event i,ITE_Assets a where DateDiff(dd,date,getdate())<=30 " +
            "and  DateDiff(dd,date,getdate())>=23 " +
            "and event = 5 " +
            "and a.itemid = i.itemid " +
            "and a.state = 2",nativeQuery = true)
    List<Event> findCloseEvent();
}
