package com.icss.sys.utils.user;

import com.icss.entity.SysUser;
import com.icss.service.SysUserService;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.utils.admin.SpringContextHolder;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.List;

/**
 * 小程序工具类
 */
public class UserUtils {

    //同步登录账号:为了方便权限管理
    public static ResultInfo insert(String id, String loginName, String password, String roleCode){
        SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");
        SysRoleService sysRoleService = SpringContextHolder.getBean("sysRoleService");
        //根据登录名判断是否有用户
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setLoginName(loginName);
        sysUser.setPassword(password);
        SysUser nameRegister = sysUserService.findByLoginName(sysUser.getLoginName());
        if (nameRegister != null) {//已经被注册过
            return ResultInfo.error("注册失败,该登录名已经被注册过");
        }
        ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
        String passwordMd5 = new SimpleHash("MD5", sysUser.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
        sysUser.setPassword(passwordMd5);
        sysUserService.insert(sysUser);
        //更新当前用户所有角色
        SysRole sysRole = new SysRole();
        sysRole.setRoleCode(roleCode);
        List<SysRole> list = sysRoleService.getList(sysRole);
        sysUser.setRoleList(list);
        sysUserService.updateUserRoles(sysUser);
        return ResultInfo.ok("提交成功");
    }

    //同步登录账号:为了方便权限管理
    public static ResultInfo update(String id, String loginName, String password, String roleCode){
        SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");
        //根据登录名判断是否有用户
        SysUser sysUser = sysUserService.get(id);
        if(sysUser==null){
            insert(id,loginName,password,roleCode);
        }else{
            sysUser.setLoginName(loginName);
            sysUser.setPassword(password);
            ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
            String passwordMd5 = new SimpleHash("MD5", sysUser.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
            sysUser.setPassword(passwordMd5);
            sysUserService.update(sysUser);
        }
        return ResultInfo.ok("提交成功");
    }
    //同步登录账号:为了方便权限管理
    public static void delete(String id) {
        SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");
        //根据登录名判断是否有用户
        sysUserService.delete(id);
    }
    //同步登录账号:为了方便权限管理
    public static void delAll(String[] ids) {
        SysUserService sysUserService = SpringContextHolder.getBean("sysUserService");
        for (String id : ids) {
            sysUserService.delete(id);
        }
    }

}
