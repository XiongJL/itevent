package com.liwinon.itevent;

import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.util.setExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IteventApplicationTests {
    @Autowired
    AssetsDao assetsDao;
    @Autowired
    EventDao eventDao;
    @Autowired
    setExcel excel;
    @Autowired
    SapDao sapDao;
    @Test
    public void contextLoads() {
        //将所有有userid,没有name 的添加name
//        List<Assets> all =assetsDao.findAll();
//        for (Assets a:all){
//            if(!StringUtils.isEmpty(a.getUserid())){
//                if (StringUtils.isEmpty(a.getUsername())){
//                    String name = sapDao.findNByUserId(a.getUserid());
//                    a.setUsername(name);
//                    assetsDao.save(a);
//                }
//            }
//        }
    }

}
