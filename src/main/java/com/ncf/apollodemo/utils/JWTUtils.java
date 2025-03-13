package com.ncf.apollodemo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static final String SIGNATURE = "!@#$SGW^HDY*%G";

    /**
     * 生成token  header.payload.sing
     */
    public static String getToken(Map<String,String> map){
        //默认7天过期
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);
        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //设置payload
        map.forEach(builder::withClaim);
        //指定令牌过期时间
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token 合法性
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

}