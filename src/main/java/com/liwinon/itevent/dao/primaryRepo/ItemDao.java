package com.liwinon.itevent.dao.primaryRepo;

import com.liwinon.itevent.entity.primary.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemDao extends JpaRepository<Item,String>, JpaSpecificationExecutor<Item> {
    //查询所有的物料类型
    @Query(value = "SELECT DISTINCT type FROM ITE_Item",nativeQuery = true)
    List<String> findAllTypes();
    //查询类型对应的品牌型号
    @Query(value = "SELECT brand FROM  ITE_Item i WHERE i.type = :type",nativeQuery = true)
    List<String> brandByType(String type);
    //查询类型对应的计量单位
    @Query(value = "SELECT DISTINCT unit FROM  ITE_Item i WHERE i.type = :type",nativeQuery = true)
    List<String> unitByType(String type);
    //根据type,brand查找物料
    Item findByTypeAndBrand(String type, String brand);

    Item findByItemid(String itemid);

    //计算物料种类
    @Query(value = "select count(distinct i.type) from Item i")
    long countItemType();
}
