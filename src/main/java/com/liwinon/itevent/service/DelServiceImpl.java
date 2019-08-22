package com.liwinon.itevent.service;

import com.liwinon.itevent.dao.primaryRepo.AssetsDao;
import com.liwinon.itevent.entity.primary.Assets;
import com.liwinon.itevent.service.DelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DelServiceImpl implements DelService {
    @Autowired
    AssetsDao assetsDao;

    /**
     * 根据资产的序号参数
     * @param id
     * @return
     */
    @Override
    @Transactional
    public String delAssets(String id) {
        if (StringUtils.isEmpty(id)){
            return "Null id";
        }
        int aid = Integer.valueOf(id);
        Assets assets = assetsDao.findById(aid);
        if (assets!=null){
            assetsDao.delete(assets);
            return "ok";
        }
        return "Assets null";
    }
}
