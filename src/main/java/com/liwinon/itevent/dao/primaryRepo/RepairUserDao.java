package com.liwinon.itevent.dao.primaryRepo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.itevent.entity.primary.RepairUser;

public interface RepairUserDao extends JpaRepository<RepairUser,String>, JpaSpecificationExecutor<RepairUser>{
	
	@Query(value = "select  a.userid from ITE_RepairUser a where a.userlevel  = 1 and a.team= :team",nativeQuery = true)
	 String[]  findAllUserid(String team);
	
	@Query(value = "select  a.userid from ITE_RepairUser a where a.userlevel  = 2 and a.team= :team",nativeQuery = true)
	String[]  findAllUseridrep(String team);

	RepairUser findByPersonid(String personid);

	@Query(value = "select u from RepairUser u where" +
			" u.userlevel=:myLevel and u.team = :team")
	List<RepairUser> findByUserlevelAndTeam(int myLevel, String team);

	List<RepairUser> findByTeam(String team);

}
