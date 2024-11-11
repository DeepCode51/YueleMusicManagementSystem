package com.icss.sys.utils.jwt;

import com.icss.entity.Register;
import com.icss.sys.utils.admin.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

public class JwtUtils {

    private static final String SECRET = "suifeng";

    /**
     * 生成token
     * @param payload 载荷,需要在token中存放的数据
     * @return
     */
    public static String generateToken(Map<String, String> payload) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();
        //载荷,生成token中保存的信息
        payload.forEach(builder::withClaim);
        return builder.withAudience("admin") //签发对象
                .withIssuedAt(new Date()) //发行时间
                .withExpiresAt(instance.getTime()) //过期时间
                .sign(Algorithm.HMAC256(SECRET)); //加密算法+盐
    }

    /**
     * 校验token,有异常,即为校验失败
     * @param token token数据
     * @return
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    //获取客户端用户id
    public static String getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if(StringUtils.isNotEmpty(token)){
            DecodedJWT jwt = JWT.decode(token);
            //System.out.println("获得的userId:"+jwt.getClaim("userId").asString());
            return jwt.getClaim("userId").asString();
        }else {
            return "";
        }
    }

    /**
     * 设置token
     * @return
     */
    public static String createToken(Register register, String form) {
        Map<String,String> map = new HashMap<>();
        map.put("userId", register.getId());
        map.put("loginName", register.getLoginName());
        map.put("from", form);
        String token = JwtUtils.generateToken(map);
        return token;
    }
}