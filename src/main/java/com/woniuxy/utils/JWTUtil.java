package com.woniuxy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

//目的：将创建和解析jwt的代码抽取出来，实现代码的复用
public class JWTUtil {


    //设置过期时间
    private static final Long EXPIRE_TIME=30000L;

    //设置加密
    private  static  final  String SIGN = SaltUtils.getSalt(16);


    //创建jwt
    public static String createToken(HashMap<String,String> map){

        //创建JWT对象
        JWTCreator.Builder builder = JWT.create();
        //设置有效负载
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        //设置过期时间
        builder.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_TIME));

        //加密，并返回令牌
        String token = builder.sign(Algorithm.HMAC256(SIGN));

        return token;
    }

    //解析jwt
    public  static DecodedJWT decodedJWT(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }


}
