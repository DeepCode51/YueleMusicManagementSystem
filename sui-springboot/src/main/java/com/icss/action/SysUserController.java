package com.icss.action;

import com.icss.entity.SysUser;
import com.icss.service.SysUserService;
import com.icss.sys.base.constant.AdminConst;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.utils.admin.ShiroUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *【后台用户】页面接口
 */
@RestController
@RequestMapping("/admin/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 【后台用户】根据条件分页查询
     * @param page
     * @param sysUser
     * @return
     */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysUser:getPage")
    public ResultInfo getPage(Page<SysUser> page, SysUser sysUser) {
        IPage<SysUser> iPage = sysUserService.getPage(page, sysUser);
        return ResultInfo.ok("获取分页成功", iPage);
    }


    /**
     * 【后台用户】根据条件查询
     * @param sysUser
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(SysUser sysUser) {
        List<SysUser> list = sysUserService.getList(sysUser);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【后台用户】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysUser:edit","sysUser:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysUser sysUser = sysUserService.get(id);
        return ResultInfo.ok("获取对象成功", sysUser);
    }

    /**
     * 【后台用户】提交(新增或修改)
     * @param sysUser
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("sysUser:save")
    public ResultInfo insert(SysUser sysUser) {
        if (StringUtils.isEmpty(sysUser.getId())) { //新增
            SysUser nameRegister = sysUserService.findByLoginName(sysUser.getLoginName());
            if (nameRegister != null) {//已经被注册过
                return ResultInfo.error("提交失败,该登录名已经被注册过");
            }
            sysUser.setPassword("123456");
            ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
            String passwordMd5 = new SimpleHash("MD5", sysUser.getPassword(), ByteSource.Util.bytes(salt), 1024).toString();
            sysUser.setPassword(passwordMd5);
            sysUserService.insert(sysUser);
        } else {//修改
            sysUserService.update(sysUser);
        }
        //更新当前用户所有角色
        sysUserService.updateUserRoles(sysUser);
        return ResultInfo.ok("提交成功!");
    }


    /**
     * 【后台用户】删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysUser:delete")
    public ResultInfo delete(String id) {
        sysUserService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【后台用户】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    @RequiresPermissions("sysUser:delete")
    public ResultInfo delAll(String[] ids) {
        sysUserService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     * 用户信息修改
     * @param userInfo
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public ResultInfo updateRegister(SysUser userInfo) {
        String currentUserId = ShiroUtils.getUserInfo().getId();
        if (!StringUtils.isEmpty(currentUserId)) {
            userInfo.setId(currentUserId);
            sysUserService.update(userInfo);//修改
            return ResultInfo.ok("更新成功！",userInfo);
        }
        return ResultInfo.ok("更新失败！");
    }
    //根据id获取用户角色
    @RequestMapping("/findCheckedRolesByUserId")
    public ResultInfo findRolesListByUserId(String userId) {
        List<SysRole> roles = sysRoleService.findCheckedRolesByUserId(userId);
        return ResultInfo.ok("获取成功!",roles);
    }

    /**
     * 修改密码
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPsd")
    public ResultInfo modifyPsd(HttpServletRequest request) {
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
        SysUser userInfo = ShiroUtils.getUserInfo();
        if (userInfo != null) {
            ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
            String passwordMd5 = new SimpleHash("MD5", oldPassword, ByteSource.Util.bytes(salt), 1024).toString();
            if (!passwordMd5.equals(userInfo.getPassword())) {
                return ResultInfo.error("旧密码错误！");
            } else {
                String passwordNew = new SimpleHash("MD5", prePassword, ByteSource.Util.bytes(salt), 1024).toString();
                userInfo.setPassword(passwordNew);
                sysUserService.update(userInfo);
                return ResultInfo.ok("密码修改成功！");
            }
        }
        return ResultInfo.ok("密码修改失败！");
    }

    //根据id重置密码
    @RequestMapping("/resetPassword")
    public ResultInfo resetPassword(String userId) {
        SysUser sysUser = sysUserService.get(userId);
        ByteSource salt = ByteSource.Util.bytes(AdminConst.SALT);
        String md5PrePassword = new SimpleHash("MD5", "123456", ByteSource.Util.bytes(salt), 1024).toString();
        sysUser.setPassword(md5PrePassword);
        sysUserService.update(sysUser);
        return ResultInfo.ok("重置成功,该用户的新密码为【123456】");
    }
    //根据id冻结用户
    @RequestMapping("/lockUser")
    public ResultInfo lockUser(SysUser entity) {
        int disabled = entity.getDisabled();
        SysUser sysUser = sysUserService.get(entity.getId());
        if(disabled==1){//冻结
            sysUser.setDisabled(1);
            sysUserService.update(sysUser);
            return ResultInfo.ok("设置成功，该用户将不能登录了。");
        }else{//释放
            sysUser.setDisabled(0);
            sysUserService.update(sysUser);
            return ResultInfo.ok("设置成功，该用户可以重新登录了。");
        }
    }

    /**
     *【后台用户】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysUser:export")
    public void exportFile(SysUser sysUser, HttpServletResponse response) {
        try {
            String fileName = "后台用户" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysUser> list = sysUserService.getList(sysUser);
            new ExportExcel("后台用户", SysUser.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【后台用户】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysUser:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysUser> list = ei.getDataList(SysUser.class);
            for (SysUser sysUser : list) {
                try {
                    sysUserService.insert(sysUser);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条用户信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条用户信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入用户信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【后台用户】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysUser:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "用户信息数据导入模板.xlsx";
            List<SysUser> list = new ArrayList<>();
            new ExportExcel("用户信息数据", SysUser.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }



}



