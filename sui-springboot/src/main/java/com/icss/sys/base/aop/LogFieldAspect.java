package com.icss.sys.base.aop;

import com.icss.entity.SysUser;
import com.icss.sys.base.annotation.LogField;
import com.icss.sys.base.module.log.entity.SysLog;
import com.icss.sys.base.module.log.service.SysLogService;
import com.icss.sys.utils.admin.ShiroUtils;
import com.icss.sys.utils.admin.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogFieldAspect {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 配置切入点，该方法无方法体，主要为了方便同类中其他方法使用此处配置的切入点
     */
    @Pointcut("@annotation(com.icss.sys.base.annotation.LogField)")
    public void pointcut() {

    }

    /**
     * aop前置
     *
     * @param joinPoint
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
    }

    /**
     * aop后置
     *
     * @param joinPoint
     */
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {

    }

    /**
     *
     */
    @Around("pointcut()")
    public Object around(JoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        SysLog sysLog = new SysLog();
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            LogField logField = method.getAnnotation(LogField.class);
            if (!StringUtils.isEmpty(logField.moduleName())) {
                sysLog.setModuleName(logField.moduleName());
                sysLogService.insert(sysLog);
                proceed = ((ProceedingJoinPoint) joinPoint).proceed();
                String methodName = joinPoint.getSignature().getName();
                if ("userLogin".equals(methodName)) {//如果登录:补全用户信息
                    SysLog entity = sysLogService.get(sysLog.getId());
                    SysUser userInfo = ShiroUtils.getUserInfo();
                    entity.setLoginName(userInfo.getLoginName());
                    entity.setUserId(userInfo.getId());
                    sysLogService.update(entity);
                }
            }
        } catch (Throwable throwable) {
            sysLogService.delete(sysLog.getId());
            throwable.printStackTrace();
        }
        return proceed;
    }

    /**
     * 方法return后
     *
     * @param joinPoint
     */
    @AfterReturning("pointcut()")
    public void afterReturn(JoinPoint joinPoint) {

    }

    /**
     * 方法抛出异常是调用
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void afterThrow(JoinPoint joinPoint, Exception ex) {

    }

}
