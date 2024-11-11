package com.icss.sys.base.run;

import com.icss.sys.base.cache.service.SysCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.icss.sys.base.module.config.service.SysConfigService.getSysConfig;

/**
 * 服务启动后执行：缓存加载
 */
@Component
@Order(value = 1)
public class StartupCacheRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupCacheRunner.class);
    @Autowired
    private SysCacheService sysCacheService;
    @Override
    public void run(String... args) {
        sysCacheService.init();
        String projectName = getSysConfig("projectName");
        logger.info(">>>>>>>>>>>>>>> 恭喜你成功启动【"+projectName+"】 <<<<<<<<<<<<<");
    }
}