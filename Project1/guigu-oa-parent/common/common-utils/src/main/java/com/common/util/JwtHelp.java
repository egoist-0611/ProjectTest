package com.common.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * 生成token的工具类
 */
public class JwtHelp {
    private static long tokenExistTime = 1000 * 60 * 60;        // 定义token有效时间：60分钟
    private static String tokenSignKey = "123456";          // 定义token生成或解析时的秘钥

    /**
     * 根据用户id和用户名来生成token
     *
     * @param userId   用户id
     * @param username 用户名
     * @return token
     */
    public static String createToken(Long userId, String username) {
        return Jwts.builder()
                // 定义token过期时间（当前时间+指定时间=过期的时间点）
                .setExpiration(new Date(System.currentTimeMillis() + tokenExistTime))
                // 生成的token中包含的信息，也是token的主体部分
                .claim("userId", userId).claim("username", username)
                // 编码压缩
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP).compact();
    }

    /**
     * 获取token中的用户id
     *
     * @param token 有用户id作为主体之一的token
     * @return 用户id
     */
    public static Long getUserId(String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(tokenSignKey).parseClaimsJws(token);     // 根据秘钥解析token
        return ((Integer) jws.getBody().get("userId")).longValue();     // 获取token中的指定信息
    }

    /**
     * 获取token中的用户名
     *
     * @param token 有用户名作为主体之一的token
     * @return 用户名
     */
    public static String getUsername(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        return (String) jws.getBody().get("username");
    }

    // 测试
    public static void main(String[] args) {
        String token = createToken(2L, "Tom");
        System.out.println(token);
        System.out.println(getUsername(token));
        System.out.println(getUserId(token));
    }
}
