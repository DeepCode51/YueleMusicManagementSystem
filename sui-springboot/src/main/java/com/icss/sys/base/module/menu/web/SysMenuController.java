package com.icss.sys.base.module.menu.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.menu.entity.SysMenu;
import com.icss.sys.base.module.menu.service.SysMenuService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*【菜单信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysMenu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
    * 【菜单信息】根据条件分页查询
    * @param page
    * @param sysMenu
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SysMenu> page, SysMenu sysMenu) {
        IPage<SysMenu> iPage = sysMenuService.getPage(page, sysMenu);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    @RequestMapping("/getTreePage")
    public ResultInfo getTreePage(Page<SysMenu> page, SysMenu sysMenu) {
        sysMenu.setPid("1");
        IPage<SysMenu> iPage = sysMenuService.getPage(page, sysMenu);
        List<SysMenu> treeList = sysMenuService.getMenuChildren(iPage.getRecords());
        iPage.setRecords(treeList);
        return ResultInfo.ok("获取分页成功", iPage);
    }


    /**
    * 【菜单信息】根据条件查询
    * @param sysMenu
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysMenu sysMenu) {
        List<SysMenu> list = sysMenuService.getList(sysMenu);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【菜单信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(String id) {
        SysMenu sysMenu = sysMenuService.get(id);
        return ResultInfo.ok("获取对象成功", sysMenu);
    }

    /**
    * 【菜单信息】提交(新增或修改)
    * @param sysMenu
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(SysMenu sysMenu) {
        if (StringUtils.isEmpty(sysMenu.getId())) { //新增
            sysMenuService.insert(sysMenu);
        } else {//修改
            sysMenuService.update(sysMenu);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【菜单信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        sysMenuService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【菜单信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysMenuService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     * 提供组件菜单树
     * @return
     */
    @RequestMapping("/getMenuTree")
    public ResultInfo getMenuTree(SysMenu sysMenu) {
        List<SysMenu> root = sysMenuService.getList(sysMenu);
        List<SysMenu> treeList = sysMenuService.getMenuChildren(root);
        return ResultInfo.ok("获取列表成功", treeList);
    }
    /**
     *【菜单信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysMenu:export")
    public void exportFile(SysMenu sysMenu, HttpServletResponse response) {
        try {
            String fileName = "菜单信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysMenu> list = sysMenuService.getList(sysMenu);
            new ExportExcel("菜单信息", SysMenu.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【菜单信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysMenu:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysMenu> list = ei.getDataList(SysMenu.class);
            for (SysMenu sysMenu : list) {
                try {
                    sysMenuService.insert(sysMenu);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条菜单信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条菜单信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入菜单信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【菜单信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysMenu:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "菜单信息数据导入模板.xlsx";
            List<SysMenu> list = new ArrayList<>();
            new ExportExcel("菜单信息数据", SysMenu.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }


}



