package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface ScoreDao extends JpaRepository<Score,String>, JpaSpecificationExecutor<Score> {

	@Query(value = "SELECT a.uuid FROM ITE_Score a WHERE a.uuid = :uuid ",nativeQuery = true)
	String findAllUuid(String uuid);
}
