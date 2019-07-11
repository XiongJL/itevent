package com.liwinon.itevent;

import com.liwinon.itevent.dao.AssetsDao;
import com.liwinon.itevent.entity.BackModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IteventApplicationTests {
    @Autowired
    AssetsDao assetsDao;
    @Test
    public void contextLoads() {
    }

}
