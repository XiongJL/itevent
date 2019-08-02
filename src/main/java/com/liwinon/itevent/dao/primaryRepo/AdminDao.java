package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AdminDao extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {
    @Query(value = "select a from Admin a where a.username = :username")
    Admin findByUser(String username);
}
