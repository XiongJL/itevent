package com.liwinon.itevent.dao.primaryRepo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.liwinon.itevent.entity.primary.RepairUser;

public interface RepairUserDao extends JpaRepository<RepairUser,String>, JpaSpecificationExecutor<RepairUser>{
	//用组别查询维护人员，然后取出维护人员的最小等级
	@Query(value = "select top 1 min(a.userlevel) from ITE_RepairUser a where a.team = :team",nativeQuery = true)
	int findAllmin(String team);
	//用等级和组别查询对应的维护人员的userid  
	@Query(value = "select  a.userid from ITE_RepairUser a where a.userlevel  =:min and a.team = :team",nativeQuery = true)
	 String[]  findAllUserid(int min,String team);

	RepairUser findByPersonid(String personid);

	@Query(value = "select u from RepairUser u where" +
			" (u.userlevel=1 or u.userlevel=2) and u.team = :team")
	List<RepairUser> findByLevel1And2AndTeam(String team);

	List<RepairUser> findByTeam(String team);

	RepairUser findByUserid(String userid);

}
