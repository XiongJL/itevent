package com.liwinon.itevent.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.entity.primary.Admin;
import com.liwinon.itevent.exception.MyException;
import com.liwinon.itevent.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static com.liwinon.itevent.exception.ResultEnum.*;

/**
 * 拦截器, 用于校验token
 */
public class AuthenticTokenInterceptor implements HandlerInterceptor {
    @Autowired
    IndexService index;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        String url=request.getRequestURL().toString();
        System.out.println("拦截器:"+url);
        if (url.indexOf("login")>=0){  //登录页面不做验证
            return true;
        }
        HttpSession session = request.getSession();
        if (session==null ||session.getAttribute("username")==null||session.getAttribute("username")=="" ){
            String  parma = request.getQueryString();
            System.out.println("param:"+parma);
            if (url.indexOf("login")>=0){  //登录页面不做验证
                System.out.println("之前进入的登录页面,不做保存");
                return true;
            }
            String trueURL = url;
            if (parma!=null){
                trueURL += "?"+parma;
            }
            session.setAttribute("OriginUrl",trueURL);
            response.sendRedirect("/itevent/login");
            return true;
        }
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //获取方法的实体类对象
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //检查方法是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PasssToken.class)){   //.class获取类的class对象
            PasssToken passToken = method.getAnnotation(PasssToken.class);
            if (passToken.required()){
                return true;
            }
        }else{  //如果没有passtoken 则需要验证
            //认证token
            if (token==null){
                throw new MyException(ERROR_1.getCode(), ERROR_1.getMsg());
            }
            //获取token中的 user
            String user;
            try{
                user = JWT.decode(token).getAudience().get(0);
            }catch (JWTDecodeException e){
                //需要先登录,或用户或密码错误
                throw new MyException(ERROR_3.getCode(), ERROR_3.getMsg());
            }
            Admin admin = index.getAdmin(user);
            if (admin==null){
                throw new MyException(ERROR_2.getCode(), ERROR_2.getMsg());
            }
            //验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPwd())).build();
            try{
                jwtVerifier.verify(token);
            }catch (JWTVerificationException e){
                //需要先登录,或用户或密码错误
                throw new MyException(ERROR_3.getCode(), ERROR_3.getMsg());
            }
            return true;
        }
        //验证是否有UserLoginToken注解 , 有则需要认证token
//        if (method.isAnnotationPresent(UserLoginToken.class)) {
//            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
//            if (userLoginToken.required()){
//                //认证token
//                if (token==null){
//                    throw new RuntimeException("无token,请重新登录");
//                }
//                //获取token中的 user
//                String user;
//                try{
//                    user = JWT.decode(token).getAudience().get(0);
//                }catch (JWTDecodeException e){
//                    //需要先登录,或用户或密码错误
//                    throw new RuntimeException("401");
//                }
//                Admin admin = index.getAdmin(user);
//                if (admin==null){
//                    throw new RuntimeException("用户不存在,请重新登录");
//                }
//                //验证 token
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPwd())).build();
//                try{
//                    jwtVerifier.verify(token);
//                }catch (JWTVerificationException e){
//                    //需要先登录,或用户或密码错误
//                    throw new RuntimeException("401");
//                }
//                return true;
//            }
//        }
        return true;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（DispatcherServlet进行视图返回渲染之前进行调用），
     * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，
     * modelAndView也可能为null。
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求处理完毕回调方法,该方法也是需要当前对应的Interceptor的preHandle()的返回值为true时才会执行，
     * 也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 用于进行资源清理。整个请求处理完毕回调方法。如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
