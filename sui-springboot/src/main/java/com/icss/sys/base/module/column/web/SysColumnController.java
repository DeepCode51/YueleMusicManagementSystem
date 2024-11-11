package com.icss.sys.base.module.column.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.column.entity.SysColumn;
import com.icss.sys.base.module.column.service.SysColumnService;
import com.icss.sys.utils.admin.StringUtils;
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
*【字段信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysColumn")
public class SysColumnController extends BaseController {

    @Autowired
    private SysColumnService sysColumnService;

    /**
    * 【字段信息】根据条件分页查询
    * @param page
    * @param sysColumn
    * @return
    */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysColumn:getPage")
    public ResultInfo getPage(Page<SysColumn> page, SysColumn sysColumn) {
        IPage<SysColumn> iPage = sysColumnService.getPage(page, sysColumn);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【字段信息】根据条件查询
    * @param sysColumn
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysColumn sysColumn) {
        List<SysColumn> list = sysColumnService.getList(sysColumn);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【字段信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysColumn:edit","sysColumn:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysColumn sysColumn = sysColumnService.get(id);
        return ResultInfo.ok("获取对象成功", sysColumn);
    }

    /**
    * 【字段信息】提交(新增或修改)
    * @param sysColumn
    * @return
    */
    @RequestMapping("/sub")
    @RequiresPermissions("sysColumn:save")
    public ResultInfo insert(SysColumn sysColumn) {
        if (StringUtils.isEmpty(sysColumn.getId())) { //新增
            sysColumnService.insert(sysColumn);
        } else {//修改
            sysColumnService.update(sysColumn);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【字段信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    @RequiresPermissions("sysColumn:delete")
    public ResultInfo delete(String id) {
        sysColumnService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【字段信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    @RequiresPermissions("sysColumn:delete")
    public ResultInfo delAll(String[] ids) {
        sysColumnService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }


    /**
    *【字段信息】导出
    */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysColumn:export")
    public void exportFile(SysColumn sysColumn, HttpServletResponse response) {
        try {
            String fileName = "字段信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysColumn> list = sysColumnService.getList(sysColumn);
            new ExportExcel("字段信息", SysColumn.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *【字段信息】导入
    */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysColumn:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysColumn> list = ei.getDataList(SysColumn.class);
            for (SysColumn sysColumn : list) {
                try {
                    sysColumnService.insert(sysColumn);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条字段信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条字段信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入字段信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
    *【字段信息】模板下载
    */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysColumn:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "字段信息数据导入模板.xlsx";
            List<SysColumn> list = new ArrayList<>();
            new ExportExcel("字段信息数据", SysColumn.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

}



