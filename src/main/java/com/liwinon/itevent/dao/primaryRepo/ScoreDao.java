package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface ScoreDao extends JpaRepository<Score,String>, JpaSpecificationExecutor<Score> {

	@Query(value = "select a.uuid from ITE_Score a where a.uuid =:uuid ",nativeQuery = true)
	String findByUuid(String uuid);
}
