package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ScoreDao extends JpaRepository<Score,String>, JpaSpecificationExecutor<Score> {
}
