package com.icss.sys.base.module.userRole.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.icss.sys.base.module.userRole.service.SysUserRoleService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*【用户-角色】页面接口
*/
@RestController
@RequestMapping("/admin/sysUserRole")
public class SysUserRoleController extends BaseController {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
    * 【用户-角色】根据条件分页查询
    * @param page
    * @param sysUserRole
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SysUserRole> page, SysUserRole sysUserRole) {
        IPage<SysUserRole> iPage = sysUserRoleService.getPage(page, sysUserRole);
        return ResultInfo.ok("获取分页成功", iPage);
    }


    /**
    * 【用户-角色】根据条件查询
    * @param sysUserRole
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysUserRole sysUserRole) {
        List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【用户-角色】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(String id) {
        SysUserRole sysUserRole = sysUserRoleService.get(id);
        return ResultInfo.ok("获取对象成功", sysUserRole);
    }

    /**
    * 【用户-角色】提交(新增或修改)
    * @param sysUserRole
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(SysUserRole sysUserRole) {
        if (StringUtils.isEmpty(sysUserRole.getId())) { //新增
            sysUserRoleService.insert(sysUserRole);
        } else {//修改
            sysUserRoleService.update(sysUserRole);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【用户-角色】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        sysUserRoleService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

}



