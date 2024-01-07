package com.littlepants.attack.attackweb.util;

import com.littlepants.attack.attackweb.entity.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description Jwt工具类
 */
public class JwtUtils {
    private static final String JWT_PAYLOAD_USER_KEY = "user";

    /**
     * 生成Token并设置过期时间（过期时间不在此处实现，在Redis中实现）
     * @param userInfo
     * @param privateKey
     * @param expire
     * @return
     */
    public static String generateTokenExpireInMillis(Object userInfo, PrivateKey privateKey,long expire){
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY,JsonUtils.toString(userInfo))
                .setId(createJTI())
//                .setExpiration(new Date(System.currentTimeMillis()+expire))//毫秒
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

    }

    /**
     * Token解密
     * @param token
     * @param publicKey
     * @return
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 从Token中获取个人信息
     * @param token
     * @param publicKey
     * @param userType
     * @return
     * @param <T>
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType){
        Jws<Claims> claimsJws = parserToken(token,publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
//        System.out.println("body"+body.get(JWT_PAYLOAD_USER_KEY).toString());
        claims.setUserinfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(),userType));
//        claims.setExpiration(body.getExpiration());
//        System.out.println(claims.getUserinfo());
        return claims;
    }

    public static <T>Payload<T> getInfoFromToken(String token,PublicKey publicKey){
        Jws<Claims> claimsJws = parserToken(token,publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 生成ID
     * @return
     */
    private static String createJTI(){
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
    }
}
