package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.entity.Model.BackModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //查询指定物料编码所有仓库可用的数量
    @Query(value = "select  count(*) from ITE_Assets where itemid=:itemid " +
            "and state = 0",nativeQuery = true)
    long countAssetsByItemid(String itemid);
    //查询指定物料编码所有仓库的所有数量(不包含废品)
    @Query(value = "select  count(*) from ITE_Assets where itemid=:itemid " +
            "and state <> 3",nativeQuery = true)
    long countAllAssetsByItemid(String itemid);


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
    List<Assets> findByUseridAndItemid(String userid, String itemid);
    //查询退还页面的  用户正在使用中的资产javaBean
    @Query(value = "select new com.liwinon.itevent.entity.Model.BackModel(a.userid,a.id,a.assetsid,i.itemid,i.type,i.brand,i.unit)" +
            " from Assets a,Item i" +
            " where userid = :userid AND (state = 1 or state = 2) and a.itemid = i.itemid")
    List<BackModel> findUseByBack(String userid);



    //查询所有的可用资产数量
    @Query(value = "select count(a) from Assets as a where a.state <> 3")
    long countAllAvailable();

    //查找资产仓可用数量
    @Query(value = "select count(a)from Assets as a where a.state <> 3 and a.store =0 ")
    long countZichanRepoNum();

    //查找费用仓可用数量
    @Query(value = "select count(a)from Assets as a where a.state <> 3 and a.store =1 ")
    long countFeiyongRepoNum();

    //计数废品数量
    @Query(value = "select count(a) from Assets as a where a.state = 3 ")
    long countScrapNum();

    /**
     * 全字段模糊查询
     */
    @Query(value = "select  * from ITE_Assets a,ITE_Item i where concat(a.assetsid,a.itemid,a.userid,i.type,i.brand) like %:search% and a.itemid = i.itemid",nativeQuery = true)
    Page<Assets> findAllBySearch(String search, Pageable pageable);
    /**多字段按时间段模糊搜索*/
    @Query(value = "select  * from ITE_Assets a,ITE_Item i where concat(a.assetsid,a.itemid,a.userid,i.type,i.brand) like %:search% and a.itemid = i.itemid" +
            " and (a.buyDate BETWEEN :date1 AND :date2)",nativeQuery = true)
    Page<Assets> findAllBySearchAndDate(String search, String date1, String date2, Pageable pageable);
    /**查询时间段的数据*/
    @Query(value = "select  * from ITE_Assets a,ITE_Item i where a.itemid = i.itemid " +
            "and (a.buyDate BETWEEN :date1 AND :date2)",nativeQuery = true)
    Page<Assets> findAllByDate(String date1, String date2, Pageable pageable);
}
