package com.icss.sys.base.interceptor;

import com.icss.sys.base.cache.utils.EhCacheUtils;
import com.icss.sys.base.exception.AppException;
import com.icss.sys.base.exception.TokenException;
import com.icss.sys.utils.jwt.JwtUtils;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = request.getHeader("token");
            if (token == null) {//请求头部没有token:退出操作|没有登录过
                throw new AppException("您还未登录，请先进行登录。");
            }
            JwtUtils.verify(token);//验证令牌
            String currentUserId = JwtUtils.getCurrentUserId();
            Object userToken = EhCacheUtils.get(currentUserId);
            if (userToken != null) {//token没有过期且token没被移除
                return true;
            }
            throw new TokenExpiredException("token已过期,请重新登录。");
        } catch (SignatureVerificationException e) {
            throw new AppException("无效签名");
        } catch (TokenExpiredException e) {
            throw new TokenException("token已过期,请重新登录。");
        } catch (AlgorithmMismatchException e) {
            throw new AppException("token算法不一致");
        } catch (AppException e) {
            throw new AppException("您还未登录，请先进行登录。");
        } catch (Exception e) {
            throw new AppException("token验证异常,请联系管理员。");
        }
    }
}