package com.icss.sys.base.module.office.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.office.entity.SysOffice;
import com.icss.sys.base.module.office.service.SysOfficeService;
import com.icss.sys.utils.admin.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*【机构信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysOffice")
public class SysOfficeController extends BaseController {

    @Autowired
    private SysOfficeService sysOfficeService;

    /**
    * 【机构信息】根据条件分页查询
    * @param page
    * @param sysOffice
    * @return
    */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysOffice:getPage")
    public ResultInfo getPage(Page<SysOffice> page, SysOffice sysOffice) {
        IPage<SysOffice> iPage = sysOfficeService.getPage(page, sysOffice);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    @RequestMapping("/getTreePage")
    @RequiresPermissions("sysOffice:getPage")
    public ResultInfo getTreePage(Page<SysOffice> page, SysOffice sysOffice) {
        IPage<SysOffice> iPage = sysOfficeService.getPage(page,sysOffice);
        List<SysOffice> treeList = sysOfficeService.getOfficeChildren(iPage.getRecords());
        iPage.setRecords(treeList);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【机构信息】根据条件查询
    * @param sysOffice
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysOffice sysOffice) {
        List<SysOffice> list = sysOfficeService.getList(sysOffice);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【机构信息】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysOffice:edit","sysOffice:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysOffice sysOffice = sysOfficeService.get(id);
        return ResultInfo.ok("获取对象成功", sysOffice);
    }

    /**
     * 【机构信息】提交(新增或修改)
     * @param sysOffice
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("sysOffice:save")
    public ResultInfo insert(SysOffice sysOffice) {
        if (StringUtils.isEmpty(sysOffice.getId())) { //新增
            sysOfficeService.insert(sysOffice);
        } else {//修改
            sysOfficeService.update(sysOffice);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
     * 获取机构树
     * @return
     */
    @RequestMapping("/getOfficeTree")
    public ResultInfo getOfficeTree(SysOffice sysOffice) {
        List<SysOffice> root = sysOfficeService.getList(sysOffice);
        List<SysOffice> treeList = sysOfficeService.getOfficeChildren(root);
        return ResultInfo.ok("获取列表成功", treeList);
    }


    /**
    * 【机构信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    @RequiresPermissions("sysOffice:delete")
    public ResultInfo delete(String id) {
        sysOfficeService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【机构信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysOfficeService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



