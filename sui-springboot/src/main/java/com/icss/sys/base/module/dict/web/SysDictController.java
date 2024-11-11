package com.icss.sys.base.module.dict.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.dict.entity.SysDict;
import com.icss.sys.base.module.dict.service.SysDictService;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.Logical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *【字典信息】页面接口
 */
@RestController
@RequestMapping("/admin/sysDict")
public class SysDictController extends BaseController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 【字典信息】根据条件分页查询
     * @param page
     * @param sysDict
     * @return
     */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysDict:getPage")
    public ResultInfo getPage(Page<SysDict> page, SysDict sysDict) {
        IPage<SysDict> iPage = sysDictService.getPage(page, sysDict);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    @RequestMapping("/getTreePage")
    public ResultInfo getTreePage(Page<SysDict> page, SysDict sysDict) {
        sysDict.setPid("0");
        IPage<SysDict> iPage = sysDictService.getPage(page, sysDict);
        List<SysDict> treeList = sysDictService.getMenuChildren(iPage.getRecords());
        iPage.setRecords(treeList);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
     * 【字典信息】根据条件查询
     * @param sysDict
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(SysDict sysDict) {
        List<SysDict> list = sysDictService.getList(sysDict);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【字典信息】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysDict:edit","sysDict:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysDict sysDict = sysDictService.get(id);
        return ResultInfo.ok("获取对象成功", sysDict);
    }

    /**
     * 【字典信息】提交(新增或修改)
     * @param sysDict
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("sysDict:save")
    public ResultInfo insert(SysDict sysDict) {
        if (StringUtils.isEmpty(sysDict.getId())) { //新增
            sysDictService.insert(sysDict);
        } else {//修改
            sysDictService.update(sysDict);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
     * 【字典信息】删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysDict:delete")
    public ResultInfo delete(String id) {
        sysDictService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【字典信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    @RequiresPermissions("sysDict:delete")
    public ResultInfo delAll(String[] ids) {
        sysDictService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     *【字典信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysDict:export")
    public void exportFile(SysDict sysDict, HttpServletResponse response) {
        try {
            String fileName = "字典信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysDict> list = sysDictService.getList(sysDict);
            new ExportExcel("字典信息", SysDict.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【字典信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysDict:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysDict> list = ei.getDataList(SysDict.class);
            for (SysDict sysDict : list) {
                try {
                    sysDictService.insert(sysDict);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条字典信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条字典信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入字典信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【字典信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysDict:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "字典信息数据导入模板.xlsx";
            List<SysDict> list = new ArrayList<>();
            new ExportExcel("字典信息数据", SysDict.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }


}



