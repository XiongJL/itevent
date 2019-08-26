package com.liwinon.itevent.dao.primaryRepo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.liwinon.itevent.entity.primary.EventType;

public interface EventTypeDao extends JpaRepository<EventType,Integer>, JpaSpecificationExecutor<EventType>{
	//事件分类等级   1
	@Query(value = "select  distinct level_1  from ITE_EventType where eTypeId > 9",nativeQuery = true)
	List findAlllevel_1();
	
	//事件分类等级   2
	@Query(value = "select   level_2 from ITE_EventType where level_1  =:value",nativeQuery = true)
	List findAlllevel_2(String value);
	
	//详细的事件描述
	@Query(value = "select   description from ITE_EventType where level_2  =:value",nativeQuery = true)
	String findAllldescription(String value);
	
	//事件分类的id
	@Query(value = "select   eTypeId from ITE_EventType where level_2 =:value",nativeQuery = true)
	Integer findAllBylevel_2(String value);
	
	//用事件分类id查询对应的事件分类数据
	@Query(value = "select * from ITE_EventType  where eTypeId = :id",nativeQuery = true)
	EventType findByETypeId(int id);
	
	//用详细描述查询事件对应的组别    运维   erp
	@Query(value = "select team from ITE_EventType  where description = :description",nativeQuery = true)
	String findallTeam(String description);
}
