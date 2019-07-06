package com.liwinon.itevent.dao;


import com.liwinon.itevent.entity.Sap_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SapDao extends JpaRepository<Sap_Users,String> {
    @Query(value = "select s.NAME,s.ORGTXT from Sap_Users s where s.PERSONID = :PERSONID")
    String findNDByUserId(String PERSONID);
}
