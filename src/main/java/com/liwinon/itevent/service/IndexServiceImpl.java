package com.liwinon.itevent.service;

import com.auth0.jwt.JWT;
import com.liwinon.itevent.Token.TokenUtil;
import com.liwinon.itevent.dao.AdminDao;
import com.liwinon.itevent.entity.Admin;
import com.liwinon.itevent.entity.Assets;
import com.liwinon.itevent.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.liwinon.itevent.exception.ResultEnum.ERROR_3;
import static com.liwinon.itevent.exception.ResultEnum.ERROR_4;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    AdminDao adminDao;
    @Autowired
    ApiService api;
    @Override
    /**
     * 登录验证, 并添加token ,和Session登录状态
     */
    public String login(String user, String pwd, HttpServletRequest request) {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd))
            return null;

        Admin admin = getAdmin(user);
        if (admin == null){  // 无该账户
            throw new MyException(ERROR_4.getCode(), ERROR_4.getMsg());
        }
        if (!(admin.getPwd().equals(pwd))){  //密码不同
            throw new MyException(ERROR_3.getCode(), ERROR_3.getMsg());
        }
        //正确 , 添加token
        String token = TokenUtil.getToken(admin);
        //设置session保存用户名 , 只有API接口才使用token,其余有Session验证是否登录即可
        HttpSession session = request.getSession();
        session.setAttribute("username",JWT.decode(token).getAudience().get(0));
        //请求是否有消息推送
        List<Assets> assets = api.colseAssets();
        if (assets!=null){
            session.setAttribute("msgNum",assets.size());
        }else{
            session.setAttribute("msgNum",0);
        }
        return token;
    }

    @Override
    public Admin getAdmin(String user) {
       Admin a  = adminDao.findByUser(user);
        return  a;
    }
}
