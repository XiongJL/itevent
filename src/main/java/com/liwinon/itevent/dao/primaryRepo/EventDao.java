package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventDao extends JpaRepository<Event,String>, JpaSpecificationExecutor<Event> {
    //查询7天内即将逾期的且是在外借用状态的事件 , 但是不能知道具体的型号,有可能归还了某几个
    //需要通过事件号再去出入表具体查询剩余归还数量

    /**
     * select DISTINCT i.* from ITE_Event i,ITE_Assets a where DateDiff(dd,date,getdate())<=30 and  DateDiff(dd,date,getdate())>=23 and event = 5
     * and a.itemid = i.itemid
     * and a.state = 2
     * @return
     */
    @Query(value = "select DISTINCT i.* from ITE_Event i,ITE_Assets a where  " +
            "DateDiff(dd,date,getdate())>=23 " +
            "and event = 5 " +
            "and a.itemid = i.itemid " +
            "and a.state = 2",nativeQuery = true)
    List<Event> findCloseEvent();

    //根据uuid前部分,模糊查找相同的事件
    @Query(value = "select * from ITE_Event where uuid LIKE %:uuid%",nativeQuery = true)
    List<Event> findLikeUuid(String uuid);

    @Query(value = "select year(date) as 年, " +
            "month(date) as 月, " +
            "count(*) as 数量 " +
            "from ITE_Event " +
            "where year([date])=:year " +
            "group by  year(date), month(date)",nativeQuery = true)
    List<String[]> countMothOfYear(int year);
    /**
     * 多字段模糊查询
     */
    @Query(value = "select  * from ITE_Event e where concat(e.uuid,e.itemid,e.event,e.userid,e.adminuser,e.oaid,e.orderid) like %:search%",nativeQuery = true)
    Page<Event> findMostBySearch(String search, Pageable pageable);
    /**根据日期多字段模糊查询*/
    @Query(value = "select  * from ITE_Event e where concat(e.uuid,e.itemid,e.event,e.userid,e.adminuser,e.oaid,e.orderid) like %:search%" +
            " and (convert(varchar(100),e.date,23) BETWEEN :date1 AND :date2)",nativeQuery = true)
    Page<Event> findMostBySearchAndDate(String search, String date1, String date2, Pageable pageable);
    /**
     * 查询的是事件类型
     */
    @Query(value = "select * from ITE_Event e where e.event=:event",nativeQuery = true)
    Page<Event> findByEvent(int event, Pageable pageable);
    /**根据日期查询事件类型*/
    @Query(value = "select * from ITE_Event e where e.event=:event and " +
            "(convert(varchar(100),e.date,23) BETWEEN :date1 AND :date2)",nativeQuery = true)
    Page<Event> findByEventAndDate(int event, String date1, String date2, Pageable pageable);

    /**查询时间段的数据*/
    @Query(value = "select  * from ITE_Event e where " +
            "convert(varchar(100),e.date,23) BETWEEN :date1 AND :date2",nativeQuery = true)
    Page<Event> findAllByDate(String date1, String date2, Pageable pageable);

    @Query(value = "select distinct e.phone from ITE_Event e where e.userid =:userid",nativeQuery = true)
    String[] findPhoneByUserid(String userid);

	Event findByUserid(String userid);
}
