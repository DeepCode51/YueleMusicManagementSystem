package com.icss.sys.base.config;

import com.icss.sys.base.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtConfig implements WebMvcConfigurer {

    /**
     * 添加自定义的拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/register/**") //拦截路径
                .addPathPatterns("/api/message/**") //拦截路径
                .excludePathPatterns("/api/message/get") //登录接口排除
                .excludePathPatterns("/api/message/getList") //登录接口排除
                .excludePathPatterns("/api/message/getTreePage") //登录接口排除
                .excludePathPatterns("/api/message/getTreeList") //登录接口排除
                .excludePathPatterns("/api/message/subViewRecord") //登录接口排除
                .excludePathPatterns("/api/register/getList")//登录接口排除
                .excludePathPatterns("/api/register/getLoginInfo"); //登录接口排除
    }
}
