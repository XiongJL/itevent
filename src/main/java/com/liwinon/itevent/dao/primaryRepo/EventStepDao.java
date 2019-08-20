package com.liwinon.itevent.dao.primaryRepo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.liwinon.itevent.entity.primary.EventStep;

public interface EventStepDao extends JpaRepository<EventStep,Integer>, JpaSpecificationExecutor<EventStep>{

}
