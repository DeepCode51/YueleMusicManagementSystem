package com.icss.sys.base.module.roleMenu.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.icss.sys.base.module.roleMenu.service.SysRoleMenuService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*【角色-菜单】页面接口
*/
@RestController
@RequestMapping("/admin/sysRoleMenu")
public class SysRoleMenuController extends BaseController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
    * 【角色-菜单】根据条件分页查询
    * @param page
    * @param sysRoleMenu
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SysRoleMenu> page, SysRoleMenu sysRoleMenu) {
        IPage<SysRoleMenu> iPage = sysRoleMenuService.getPage(page, sysRoleMenu);
        return ResultInfo.ok("获取分页成功", iPage);
    }


    /**
    * 【角色-菜单】根据条件查询
    * @param sysRoleMenu
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysRoleMenu sysRoleMenu) {
        List<SysRoleMenu> list = sysRoleMenuService.getList(sysRoleMenu);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【角色-菜单】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(String id) {
        SysRoleMenu sysRoleMenu = sysRoleMenuService.get(id);
        return ResultInfo.ok("获取对象成功", sysRoleMenu);
    }

    /**
    * 【角色-菜单】提交(新增或修改)
    * @param sysRoleMenu
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(SysRoleMenu sysRoleMenu) {
        if (StringUtils.isEmpty(sysRoleMenu.getId())) { //新增
            sysRoleMenuService.insert(sysRoleMenu);
        } else {//修改
            sysRoleMenuService.update(sysRoleMenu);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【角色-菜单】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        sysRoleMenuService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

}



