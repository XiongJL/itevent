package com.liwinon.itevent.dao.primaryRepo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liwinon.itevent.entity.primary.EventType;

public interface EventTypeDao extends JpaRepository<EventType,Integer>, JpaSpecificationExecutor<EventType>{
	@Query(value = "select  distinct level_1  from ITE_EventType ",nativeQuery = true)
	List findAlllevel_1();
	
	@Query(value = "select   level_2 from ITE_EventType where level_1  =:value",nativeQuery = true)
	List findAlllevel_2(String value);
	
	@Query(value = "select   description from ITE_EventType where level_2  =:value",nativeQuery = true)
	String findAllldescription(String value);
	
	@Query(value = "select   eTypeId from ITE_EventType where level_2 =:value",nativeQuery = true)
	Integer findAllBylevel_2(String value);

	EventType findByETypeId(int id);
}
