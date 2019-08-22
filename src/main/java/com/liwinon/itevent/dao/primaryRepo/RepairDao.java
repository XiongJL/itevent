package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.RepairUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairDao extends JpaRepository<RepairUser,String> {
    RepairUser findByUserid(String userid);

    RepairUser findByWxid(String fromUserName);
}
