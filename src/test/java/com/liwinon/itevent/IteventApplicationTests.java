package com.liwinon.itevent;

import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.util.setExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IteventApplicationTests {
    @Autowired
    AssetsDao assetsDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    setExcel excel;
    @Test
    public void contextLoads() {
//        Sort sort = new Sort(Sort.Direction.ASC,"uuid");
//        Pageable pageable = PageRequest.of(0,10,sort);
//        Page<Event> page  = eventDao.findMostBySearch("7",pageable);
//        for (Event e : page.getContent()){
//            System.out.println(e);
//        }
    }

}
