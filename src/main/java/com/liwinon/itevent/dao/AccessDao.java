package com.liwinon.itevent.dao;

import com.liwinon.itevent.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccessDao extends JpaRepository<Access,String>, JpaSpecificationExecutor<Access> {
}
