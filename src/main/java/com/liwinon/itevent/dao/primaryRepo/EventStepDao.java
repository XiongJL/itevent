package com.liwinon.itevent.dao.primaryRepo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.liwinon.itevent.entity.primary.EventStep;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventStepDao extends JpaRepository<EventStep,Integer>, JpaSpecificationExecutor<EventStep>{

    @Query(value ="SELECT e from EventStep e where e.uuid = :taskid and e.step=1 ")
    EventStep findByUuidAndStep1(String taskid);

    //根据事件号搜索环节, step 降序排序
    @Query(value = "select * from ITE_EventStep where uuid=:uuid order by stepId desc ",nativeQuery = true)
    List<EventStep> findByUuid(String uuid);
    
    //事件回访时搜索事件
    @Query(value = "select * from ITE_EventStep where uuid=:uuid",nativeQuery = true)
    EventStep findAlluuid(String uuid);
    
    @Query(value = "select * from ITE_EventStep e where not EXISTS(select 1 from ITE_EventStep WHERE"
    		+ " uuid=e.uuid and step > e.step)" + 
    		" and  step > 1 and step < 40 and e.executorId=:qywxid  ORDER BY  e.stepId DESC",nativeQuery = true)
	List<EventStep> findByqywxid(String qywxid);
    //根据事件号,useid搜索环节, step 降序排序
    @Query(value = "select * from ITE_EventStep where   step > 1 and step < 40 and uuid=:uuid and executorId=:userid",nativeQuery = true)
	EventStep findByUuidAndUserid(String uuid, String userid);
    
}
