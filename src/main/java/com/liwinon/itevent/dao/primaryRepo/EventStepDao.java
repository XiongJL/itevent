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
    @Query(value = "select * from ITE_EventStep where uuid=:uuid order by step desc ",nativeQuery = true)
    List<EventStep> findByUuid(String uuid);
}
