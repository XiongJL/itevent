package com.liwinon.itevent.dao.primaryRepo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.itevent.entity.primary.RepairUser;

public interface RepairUserDao extends JpaRepository<RepairUser,String>, JpaSpecificationExecutor<RepairUser>{
	
	@Query(value = "select  userid from ITE_RepairUser where userlevel  = 1 ",nativeQuery = true)
	 String[]  findAllUserid();

	RepairUser findByPersonid(String personid);

	@Query(value = "select u from RepairUser u where" +
			" u.userlevel=:myLevel and u.team = :team")
	List<RepairUser> findByUserlevelAndTeam(int myLevel, String team);

	List<RepairUser> findByTeam(String team);
}
