package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccessDao extends JpaRepository<Access,String>, JpaSpecificationExecutor<Access> {
    Access findByEventid(String uuid);
}
