package com.icss.sys.utils.admin;


import com.icss.entity.SysUser;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.module.office.entity.SysOffice;
import com.icss.sys.base.module.role.entity.SysRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微信Shiro信息获取
 */
public class ShiroUtils {
    //获取当前登录的用户
    public static SysUser getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("sysUser");
        if(obj!=null){
            return (SysUser)obj;
        }
        return null;
    }
    /**
     * 当前用户id
     * @return
     */
    public static String getCurrentUserId() {
        String currentUserId = "";
        SysUser userInfo = getUserInfo();
        if (userInfo != null) {
            currentUserId = userInfo.getId();
        }
        return currentUserId;
    }
    /**
     * 获取权限
     * @return
     */
    public static Set<String> getPermissions(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Set<String> permsSet = null;
        Object obj = request.getSession().getAttribute(AdminConst.USER_PERMISSION);
        if (obj != null) {
            permsSet = (Set<String>) obj;
        }
        return permsSet;
    }
    /**
     * 获取角色
     * @return
     */
    public static List<String> getUserRole(){
        SysUser userInfo = getUserInfo();
        List<String> userRole = null;
        if (userInfo != null) {
            List<SysRole> roleList = userInfo.getRoleList();
            userRole = roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
        }
        return userRole;
    }
    /**
     * 获取部门
     * @return
     */
    public static SysOffice getUserOffice(){
        SysUser userInfo = getUserInfo();
        SysOffice office = null;
        if (userInfo != null) {
            office = userInfo.getOffice();
        }
        return office;
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     *
     * 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    public static void setSession(String key, Object value) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request) {
            HttpSession session = request.getSession();
            //logger.debug("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }
    public static Object getSession(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request) {
            HttpSession session = request.getSession();
            //logger.debug("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if (null != session) {
                return session.getAttribute(key);
            }
        }
        return null;
    }

}
