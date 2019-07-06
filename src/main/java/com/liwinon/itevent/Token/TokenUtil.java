package com.liwinon.itevent.Token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.liwinon.itevent.entity.Admin;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    /**
     * 获取token
     * Algorithm.HMAC256():使用HS256生成token,密钥则是用户的密码，唯一密钥的话可以保存在服务端。
     * withAudience()存入需要保存在token的信息，这里我把用户ID存入token中
     * @param admin
     * @return
     */
    public static String getToken(Admin admin){
        String token;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,7 );
        Date date = calendar.getTime();
        System.out.println("token过期时间是:"+date);
        token = JWT.create().withAudience(admin.getUser())
                .withIssuedAt(new Date())//生成签名时间
                .withExpiresAt(date)  //过期时间
                .sign(Algorithm.HMAC256(admin.getPwd()));
        /*设置头部信息 Header*/
//        .withHeader(map)
/*设置 载荷 Payload*/
       // .withClaim("loginName", "lijunkui")
//              .withIssuer("SERVICE")//签名是有谁生成 例如 服务器
//            .withSubject("this is test token")//签名的主题
//.withNotBefore(new Date())//定义在什么时间之前，该jwt都是不可用的.
//           .withAudience("APP")//签名的观众 也可以理解谁接受签名的
//          .withIssuedAt(nowDate) //生成签名的时间
//       .withExpiresAt(expireDate)//签名过期的时间
/*签名 Signature */
//         .sign(algorithm);


        return token;
    }



}
