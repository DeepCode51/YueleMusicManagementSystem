package com.icss.sys.utils.session;

import com.icss.sys.base.enums.SystemType;
import com.icss.sys.utils.admin.ShiroUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 客户端获取会话信息
 */
public class SessionUtils {

    //获取系统平台
    public static String getSystemType() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {//没认证默认为admin
                return SystemType.ADMIN.getValue();
            } else {
                DecodedJWT jwt = JWT.decode(token);
                return jwt.getClaim("from").asString();
            }
        } catch (Exception e) {//定时任务时获取不到
            return SystemType.ADMIN.getValue();
        }
    }

    //获取当前用户id
    public static String getCurrentUserId() {//预保持的时候调用
        try {
            if(getSystemType().equals(SystemType.ADMIN.getValue())){
                return ShiroUtils.getCurrentUserId();
            }else{
                return JwtUtils.getCurrentUserId();
            }
        } catch (Exception e) {//定时任务时获取不到
            return "";
        }
    }

}
