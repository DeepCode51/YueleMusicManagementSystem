package com.icss.front;

import com.icss.entity.Register;
import com.icss.service.RegisterService;
import com.icss.sys.base.cache.utils.EhCacheUtils;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端登录接口
 */
@RestController
@RequestMapping("/api/login")
public class ApiLoginController extends BaseController {
    @Autowired
    private RegisterService registerService;
    /**
     * 客户端用户登录
     *
     * @param entity
     * @return
     */
    @RequestMapping("/userLogin")
    public ResultInfo userLogin(Register entity) {
        if (StringUtils.isEmpty(entity.getLoginName())) {
            return ResultInfo.error("登录名不能为空！");
        }
        if (StringUtils.isEmpty(entity.getPassword())) {
            return ResultInfo.error("密码不能为空！");
        }
        Register params = new Register();
        params.setLoginName(entity.getLoginName());
        Register register = registerService.get(params);
        if (register != null) {
            ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
            String passwordMd5 = new SimpleHash("MD5", entity.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
            if (!(register.getPassword().equals(passwordMd5))) {//已经被注册过
                return ResultInfo.error("该用户密码不正确！");
            }
            Register userInfo = registerService.get(register.getId());
            //将用户id Base64加密后传递给前端获取token
            byte[] bytes = userInfo.getId().getBytes();
            String code = Base64.getEncoder().encodeToString(bytes);
            Map<String,Object> map = new HashMap<>();
            map.put("code",code);
            map.put("userInfo",userInfo);
            return ResultInfo.ok("登录成功",map);
        } else {
            return ResultInfo.error("该用户不存在！");
        }
    }

    /**
     * 登录成功后
     * 获取用户token
     * @param code
     * @return
     */
    @RequestMapping("/getToken")
    public ResultInfo getAppletToken(String code, String from) {
        try {
            byte[] decoded = Base64.getDecoder().decode(code);
            String userId = new String(decoded);//Base64解密
            Register register = registerService.get(userId);
            String token = JwtUtils.createToken(register,from);
            EhCacheUtils.put(userId, token);//保存token时间到了过期
            Map<String, Object> map = new HashMap();
            map.put("token", token);
            return ResultInfo.ok("获取用户凭证信息成功！", map);
        } catch (Exception e) {
            return ResultInfo.error("获取用户信息失败，异常信息：" + e.getMessage());
        }
    }

    /**
     * 注册
     * @param entity
     * @return
     */
    @RequestMapping("/register")
    public ResultInfo register(Register entity) {
        //根据登录名判断是否有用户
        if (StringUtils.isEmpty(entity.getLoginName())) {
            return ResultInfo.error("登录名不能为空！");
        }
        if (StringUtils.isEmpty(entity.getPassword())) {
            return ResultInfo.error("密码不能为空！");
        }
        Register params = new Register();
        params.setLoginName(entity.getLoginName());
        Register register = registerService.get(params);
        if (register != null) {//已经被注册过
            return ResultInfo.error("注册失败,该登录名已经被注册过");
        }
        ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
        String passwordMd5 = new SimpleHash("MD5", entity.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
        entity.setPassword(passwordMd5);
        registerService.insert(entity);
        return ResultInfo.ok("注册成功！");
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("/loginOut")
    public ResultInfo loginOut() {
        String currentUserId = JwtUtils.getCurrentUserId();
        if (StringUtils.isNotEmpty(currentUserId)) {
            EhCacheUtils.remove(currentUserId);
        }
        return ResultInfo.ok("退出成功！");
    }

}



