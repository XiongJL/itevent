package com.liwinon.itevent;

import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.dao.primaryRepo.EventDao;
import com.liwinon.itevent.dao.secondRepo.SapDao;
import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.util.setExcel;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

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
    @Autowired
    WxApi wxApi;
    @Test
    public void contextLoads() {
        //URL是点击任务卡片本身跳转的页面 , 回调接口依然是接收服务器端口
     wxApi.sendMissionToIT(new String[]{"1902268014","1907128000"},"某某的服务申请",
             " 申请类型: 软件无法正常工作<br> 申请描述: 用户填写的内容<br>" +
                     " 事件等级: 加急处理",
             "6",
             new String[]{"1","2","3"},new String[]{"拒接","接收","忽略"},new String[]{"已拒接","开始处理","已忽略"},"http://www.baidu.com");
    }


}
