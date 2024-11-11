package com.icss.front;

import com.icss.entity.Register;
import com.icss.service.RegisterService;
import com.icss.sys.base.cache.utils.EhCacheUtils;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.exception.TokenException;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *【注册用户】管理
 */
@RestController
@RequestMapping("/api/register")
public class ApiRegisterController extends BaseController {

    @Autowired
    private RegisterService registerService;

    /**
     * 【注册用户】获取列表数据
     */
    @RequestMapping("/getList")
    public ResultInfo getList(Register register) {
        List<Register> list = registerService.getList(register);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 更新用户注册信息
     * 填写或者注册
     * @param userInfo
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public ResultInfo updateRegister(Register userInfo) {
        String currentUserId = JwtUtils.getCurrentUserId();
        if (!StringUtils.isEmpty(currentUserId)) {
            registerService.update(userInfo);//修改
            return ResultInfo.ok("更新成功！",userInfo);
        }
        return ResultInfo.ok("更新失败！");
    }

    /**
     * 修改密码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPsd")
    public ResultInfo modifyPsd(HttpServletRequest request){
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        String prePassword = request.getParameter("prePassword");
        if (StringUtils.isEmpty(oldPassword)) {
            return ResultInfo.error("旧密码不能为空！");
        }
        if (StringUtils.isEmpty(newPassword)) {
            return ResultInfo.error("新密码不能为空！");
        }
        if (StringUtils.isEmpty(prePassword)) {
            return ResultInfo.error("确认密码不能为空！");
        }
        if (!newPassword.equals(prePassword)) {
            return ResultInfo.error("两次密码不一致！");
        }
        String currentUserId = JwtUtils.getCurrentUserId();
        Register register = registerService.get(currentUserId);
        if (register != null) {
            ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
            String passwordMd5 = new SimpleHash("MD5", oldPassword, ByteSource.Util.bytes(salt), 1024).toString();
            if (!passwordMd5.equals(register.getPassword())) {
                return ResultInfo.error("旧密码错误！");
            } else {
                String passwordNew = new SimpleHash("MD5", prePassword, ByteSource.Util.bytes(salt), 1024).toString();
                register.setPassword(passwordNew);
                registerService.update(register);
                return ResultInfo.ok("密码修改成功！");
            }
        }
        return ResultInfo.ok("密码修改失败！");
    }


    /**
     * 获取登录信息
     * @return
     */
    @RequestMapping("/getLoginInfo")
    public ResultInfo getLoginInfo() {
        try {
            Register userInfo = null;
            Map<String, Object> map = new HashMap<>();
            String currentUserId = JwtUtils.getCurrentUserId();
            Object token = EhCacheUtils.get(currentUserId);
            if (token != null) {
                userInfo = registerService.get(currentUserId);
                map.put("hasLogin", true);
            } else {
                map.put("hasLogin", false);
            }
            map.put("userInfo", userInfo);
            return ResultInfo.ok("获取登录信息成功！", map);
        } catch (Exception e) {
            throw new TokenException("会话过期，请重新登录！");
        }
    }
}



