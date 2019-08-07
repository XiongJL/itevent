package com.liwinon.itevent.dao.secondRepo;


import com.liwinon.itevent.entity.second.Sap_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SapDao extends JpaRepository<Sap_Users,String> {
    @Query(value = "select s.NAME,s.ORGTXT,s.TELEPHONE from Sap_Users s where s.PERSONID = :PERSONID")
    String findNDByUserId(String PERSONID);
    @Query(value = "select s.NAME from Sap_Users s where s.PERSONID = :PERSONID")
    String findNByUserId(String PERSONID);

    @Query(value = "select s from Sap_Users s where s.NAME = :name")
    List<Sap_Users> findNDByName(String name);

    @Query(value = "select s from Sap_Users s where s.NAME = :name and s.ORGTXT = :dep ")
    Sap_Users findIDPhoneByDepName(String dep, String name);
}
