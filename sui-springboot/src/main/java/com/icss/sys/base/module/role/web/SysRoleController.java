package com.icss.sys.base.module.role.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.role.dto.MenuRoleParams;
import com.icss.sys.base.module.role.dto.MenuRoleTree;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.icss.sys.base.module.userRole.service.SysUserRoleService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*【角色信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysRole")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
    * 【角色信息】根据条件分页查询
    * @param page
    * @param sysRole
    * @return
    */
    @RequestMapping("/getPage")
//    @RequiresPermissions("sysRole:getPage")
    public ResultInfo getPage(Page<SysRole> page, SysRole sysRole) {
        IPage<SysRole> iPage = sysRoleService.getPage(page, sysRole);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【角色信息】根据条件查询
    * @param sysRole
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysRole sysRole) {
        List<SysRole> list = sysRoleService.getList(sysRole);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【角色信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysRole:edit","sysRole:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysRole sysRole = sysRoleService.get(id);
        return ResultInfo.ok("获取对象成功", sysRole);
    }

    /**
    * 【角色信息】提交(新增或修改)
    * @param sysRole
    * @return
    */
    @RequestMapping("/sub")
    @RequiresPermissions("sysRole:save")
    public ResultInfo insert(SysRole sysRole) {
        if (StringUtils.isEmpty(sysRole.getId())) { //新增
            sysRoleService.insert(sysRole);
        } else {//修改
            sysRoleService.update(sysRole);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【角色信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    @RequiresPermissions("sysRole:delete")
    public ResultInfo delete(String id) {
        //判断该角色下是否有用户
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(id);
        List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
        if(list.size()>0){
            return ResultInfo.error("该角色下存在用户，不能删除");
        }
        sysRoleService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【角色信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    @RequiresPermissions("sysRole:delete")
    public ResultInfo delAll(String[] ids) {
        for (String id : ids) {
            //判断该角色下是否有用户
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(id);
            List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
            if(list.size()>0){
                return ResultInfo.error("该角色下存在用户，不能删除");
            }
        }
        sysRoleService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     * 角色权限数据
     */
    @RequestMapping("/getMenuRoleTree")
    public ResultInfo getMenuRoleTree(String roleId) {
        List<MenuRoleTree> menuRoleTreeF = sysRoleService.getMenuRoleTree("1", roleId); //查询出菜单栏目下的菜单
        sysRoleService.getChrien(menuRoleTreeF, roleId);
        return ResultInfo.ok("获取菜单树成功", menuRoleTreeF);
    }
    /**
     * 角色权限数据
     */
    @RequestMapping("/updateMenuRoleTree")
    public ResultInfo updateMenuRoleTree(MenuRoleParams menuRoleParams) {
        System.out.println(menuRoleParams);
        sysRoleService.updateMenuRoleTree(menuRoleParams);
        return ResultInfo.ok("更新菜单树成功");
    }


    /**
     *【角色信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysRole:export")
    public void exportFile(SysRole sysRole, HttpServletResponse response) {
        try {
            String fileName = "角色信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysRole> list = sysRoleService.getList(sysRole);
            new ExportExcel("角色信息", SysRole.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【角色信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysRole:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysRole> list = ei.getDataList(SysRole.class);
            for (SysRole sysRole : list) {
                try {
                    sysRoleService.insert(sysRole);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条角色信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条角色信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入角色信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【角色信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysRole:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "角色信息数据导入模板.xlsx";
            List<SysRole> list = new ArrayList<>();
            new ExportExcel("角色信息数据", SysRole.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

}



