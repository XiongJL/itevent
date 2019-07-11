package com.liwinon.itevent.dao;

import com.liwinon.itevent.entity.Assets;
import com.liwinon.itevent.entity.BackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetsDao extends JpaRepository<Assets,String>, JpaSpecificationExecutor<Assets> {
    Assets findByAssetsid(String assetsid);
    //根据资产序号查找
    @Query(value = "select a from Assets a where a.id = :aid")
    Assets findById(int aid);
    // 查询指定物料编码, 仓库 的可用资产
    @Query(value = "select  * from ITE_Assets where itemid=:itemid and store = :store " +
            "and state = 0",nativeQuery = true)
    List<Assets> findUnuseByItemid(String itemid, int store);
    //查询指定物料编码, 且不在仓的 , 可用的
    @Query(value = "SELECT * FROM ITE_Assets WHERE itemid = :itemid and (state = 1 or state = 2)",nativeQuery = true)
    List<Assets> finduseByItemid(String itemid);

    //查询所有同物料编码的且没有资产牌,且状态为出库或借用的资产
    @Query(value = "select * from ITE_Assets where itemid=:itemid and (state=1 or state=2)",nativeQuery = true)
    List<Assets> findByItemidOut(String itemid);

    //根据工号查找借用状态的
    @Query(value = "SELECT * FROM ITE_Assets WHERE userid = :userid and state = 2",nativeQuery = true)
    List<Assets> findByUseridBorrow(String userid);
    //根据工号,物料编码查询使用中的资产
    @Query(value = "SELECT * FROM ITE_Assets WHERE userid = :userid and (state = 2 or state = 1)" +
            " and itemid = :itemid",nativeQuery = true)
    List<Assets> findByUseridAndItemid(String userid,String itemid);
    //查询退还页面的  用户正在使用中的资产javaBean
    @Query(value = "select new com.liwinon.itevent.entity.BackModel(a.userid,a.id,a.assetsid,i.type,i.brand,i.unit)" +
            " from Assets a,Item i" +
            " where userid = :userid AND (state = 1 or state = 2) and a.itemid = i.itemid")
    List<BackModel> findUseByBack(String userid);
}
