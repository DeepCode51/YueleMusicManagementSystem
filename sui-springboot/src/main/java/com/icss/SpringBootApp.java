package com.icss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement//开启事务管理
//与dao层的@Mapper二选一写上即可(主要作用是扫包)
@MapperScan({"com.icss.dao","com.icss.sys.base.module.*.dao"})
public class SpringBootApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:8088");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        //mode:'history':防止地址输入路由地址404
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
                Set<ErrorPage> errorPages = new HashSet<>();
                errorPages.add(error404Page);
                factory.setErrorPages(errorPages);
            }
        };
    }

}

