package com.icss.sys.base.aop;

import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.module.log.entity.SysLog;
import com.icss.sys.base.module.log.service.SysLogService;
import com.icss.sys.base.module.menu.entity.SysMenu;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.icss.sys.base.module.config.service.SysConfigService.getSysConfig;

@Component
@Aspect
public class SystemLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    @Autowired
    private SysLogService sysLogService;

    /**
     * 切面
     */
    @Pointcut("execution(* com.icss.module.*.web.*.*(..)) || execution(* com.icss.sys.*.web.*.*(..))")
    public void controllerAspect() {

    }

    /**
     * 切点
     *
     * @param joinpoint
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinpoint) {
    }

    /**
     * 切点
     *
     * @param joinpoint
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @After("controllerAspect()")
    public void doAfter(JoinPoint joinpoint) throws Exception {
        String isOpenLog = getSysConfig("isOpenLog");
        if ("1".equals(isOpenLog)) {
            Set<SysMenu> sysMenuSet = (Set<SysMenu>) ShiroUtils.getSession(AdminConst.USER_MENU);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Map<String, Object> mapAnnotation = getControllerMethodDescription(joinpoint);
            Object obj = mapAnnotation.get("permName");
            if (ObjectUtils.isNotNull(obj)) {//只记录设置权限的日志
                String permName = obj.toString();//权限名称
                SysMenu menu = sysMenuSet.stream().filter(item -> permName.equals(item.getPermission())).collect(Collectors.toList()).get(0);
                SysLog sysLog = new SysLog();
                sysLog.setModuleName(menu.getName());
                this.asyncSaveLog(sysLog);
            }
        }
    }

    @Async
    public void asyncSaveLog(SysLog sysLog) {
        sysLogService.insert(sysLog);
    }

    /**
     * 获取请求IP
     *
     * @return
     * @throws Exception
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip != null && ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    private static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取注解中对应信息
     *
     * @param joinpoint
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodDescription(JoinPoint joinpoint) throws Exception {
        Map<String, Object> mapAnnotation = new HashMap<>();
        String targetName = joinpoint.getTarget().getClass().getName();
        String methodName = joinpoint.getSignature().getName();
        Object[] arguments = joinpoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    RequiresPermissions methodAnnotation = method.getAnnotation(RequiresPermissions.class);
                    if (methodAnnotation != null) {
                        mapAnnotation.put("logical", methodAnnotation.logical());
                        mapAnnotation.put("permName", methodAnnotation.value()[0]);
                        break;
                    }

                }
            }
        }
        return mapAnnotation;
    }
}
