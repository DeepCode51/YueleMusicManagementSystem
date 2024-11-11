package com.icss.sys.base.module.log.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.log.entity.SysLog;
import com.icss.sys.base.module.log.service.SysLogService;
import com.icss.sys.utils.admin.StringUtils;
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
*【日志信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysLog")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
    * 【日志信息】根据条件分页查询
    * @param page
    * @param sysLog
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SysLog> page, SysLog sysLog) {
        IPage<SysLog> iPage = sysLogService.getPage(page, sysLog);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【日志信息】根据条件查询
    * @param sysLog
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysLog sysLog) {
        List<SysLog> list = sysLogService.getList(sysLog);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【日志信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(String id) {
        SysLog sysLog = sysLogService.get(id);
        return ResultInfo.ok("获取对象成功", sysLog);
    }

    /**
    * 【日志信息】提交(新增或修改)
    * @param sysLog
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(SysLog sysLog) {
        if (StringUtils.isEmpty(sysLog.getId())) { //新增
            sysLogService.insert(sysLog);
        } else {//修改
            sysLogService.update(sysLog);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【日志信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        sysLogService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【日志信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysLogService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     *【日志信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysLog:export")
    public void exportFile(SysLog sysLog, HttpServletResponse response) {
        try {
            String fileName = "日志信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysLog> list = sysLogService.getList(sysLog);
            new ExportExcel("日志信息", SysLog.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【日志信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysLog:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysLog> list = ei.getDataList(SysLog.class);
            for (SysLog sysLog : list) {
                try {
                    sysLogService.insert(sysLog);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条日志信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条日志信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入日志信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【日志信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysLog:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "日志信息数据导入模板.xlsx";
            List<SysLog> list = new ArrayList<>();
            new ExportExcel("日志信息数据", SysLog.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }


}



