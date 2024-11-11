package com.icss.action;

import com.icss.entity.SysUser;
import com.icss.service.SysUserService;
import com.icss.sys.base.annotation.LogField;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.utils.admin.ShiroUtils;
import com.icss.sys.utils.admin.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/login")
public class AdminLoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * pc用户登录
     *
     * @param entity
     * @return
     */
    @RequestMapping("/userLogin")
    @LogField(moduleName = "用户登录")
    public ResultInfo userLogin(SysUser entity, HttpServletRequest request) {
        if (StringUtils.isEmpty(entity.getLoginName())) {
            return ResultInfo.error("登录名不能为空！");
        }
        if (StringUtils.isEmpty(entity.getPassword())) {
            return ResultInfo.error("密码不能为空！");
        }
        //Subject currentUser = SecurityUtils.getSubject();
        //UsernamePasswordToken token = new UsernamePasswordToken(entity.getLoginName(), entity.getPassword());
        //currentUser.login(token);
        SysUser userDB = sysUserService.findByLoginName(entity.getLoginName());
        if(userDB==null){
            return ResultInfo.error("该用户不存在");
        }
        String passwordDB = userDB.getPassword();
        ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
        String passwordMd5 = new SimpleHash("MD5", entity.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
        if (!passwordDB.equals(passwordMd5)) {
            return ResultInfo.error("密码不正确");
        }
        SysUser sysUser = userDB;
        List<SysRole> roleDBList = sysRoleService.findRolesListByUserId(sysUser.getId());
        sysUser.setRoleList(roleDBList);
        if (StringUtils.isNotEmpty(entity.getRoleCode())) {
            List<String> roleList = sysUser.getRoleList().stream().map(SysRole::getRoleCode).collect(Collectors.toList());
            boolean contains = roleList.contains(entity.getRoleCode());
            if (!contains) {
                return ResultInfo.error("没有找到该角色用户");
            }
        }
        ShiroUtils.setSession("sysUser",sysUser);
        return ResultInfo.ok("登陆成功！", sysUser);
    }

    /**
     * 用户注册
     *
     * @return
     */
    @RequestMapping("/register")
    public ResultInfo register(SysUser entity) {
        //根据登录名判断是否有用户
        if (StringUtils.isEmpty(entity.getLoginName())) {
            return ResultInfo.error("登录名不能为空！");
        }
        if (StringUtils.isEmpty(entity.getPassword())) {
            return ResultInfo.error("密码不能为空！");
        }
        if (entity.getRoleList().size() == 0) {
            return ResultInfo.error("请选择用户角色！");
        }
        SysUser nameRegister = sysUserService.findByLoginName(entity.getLoginName());
        if (nameRegister != null) {//已经被注册过
            return ResultInfo.error("注册失败,该登录名已经被注册过");
        }
        ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
        String passwordMd5 = new SimpleHash("MD5", entity.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
        entity.setPassword(passwordMd5);
        sysUserService.insert(entity);
        sysUserService.updateUserRoles(entity);
        return ResultInfo.ok("注册成功！");
    }

    /**
     * 获取注册信息
     *
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ResultInfo getUserInfo() {
        String currentUserId = ShiroUtils.getUserInfo().getId();
        if (!StringUtils.isEmpty(currentUserId)) {
            SysUser userInfo = sysUserService.get(currentUserId);
            List<SysRole> roleList = sysRoleService.findRolesListByUserId(currentUserId);
            userInfo.setRoleList(roleList);
            return ResultInfo.ok("获取成功！", userInfo);
        }
        return ResultInfo.ok("获取失败！");
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    @RequestMapping("/getLoginInfo")
    public ResultInfo getLoginInfo() {
        //判断是否有基础信息
        Map<String, Object> datas = new HashMap<>();
        SysUser userInfo = ShiroUtils.getUserInfo();
        List<String> roles = ShiroUtils.getUserRole();
        Set<String> permissions = ShiroUtils.getPermissions();
        datas.put("userInfo", userInfo);
        datas.put("roles", roles);
        datas.put("permissions", permissions);
        return ResultInfo.ok("获取登录信息成功！", datas);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @LogField(moduleName = "退出登录")
    @RequestMapping("/loginOut")
    public ResultInfo loginOut() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("sysUser");
        return ResultInfo.ok("退出成功！");
    }
}