package com.icss.sys.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 线程池配置
 * @Author: mingtian
 * @CreateDate: 2020/11/12 15:57
 * @Version: 1.0
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * 打印日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * cpu 核心数量
     */
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池配置
     *
     * @return
     */
    @Bean("taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        taskExecutor.setCorePoolSize(cpuNum);
        // 配置最大线程池数量
        taskExecutor.setMaxPoolSize(cpuNum * 2);
        /// 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(2);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        taskExecutor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        // 等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        taskExecutor.setThreadNamePrefix("thread-pool-");
        // 线程池拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 线程池初始化
        taskExecutor.initialize();
        logger.info("线程池初始化......");
        return taskExecutor;
    }

    /**
     * 重写捕获异常类
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     */
    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        //手动处理捕获的异常
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            logger.error("ExceptionMessage:{}", throwable.getMessage());
            logger.error("MethodName:{}", method.getName());
            for (Object param : obj) {
                logger.error("Parameter:{}", param);
            }
        }
    }
}