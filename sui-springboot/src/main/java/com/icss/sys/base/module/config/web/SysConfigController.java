package com.icss.sys.base.module.config.web;

import com.icss.sys.base.cache.service.SysCacheService;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.config.dto.ConfigParams;
import com.icss.sys.base.module.config.entity.SysConfig;
import com.icss.sys.base.module.config.service.SysConfigService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import com.icss.sys.utils.weixni.OfficialApiUtils;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*【系统配置信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysConfig")
public class SysConfigController extends BaseController {

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysCacheService sysCacheService;

    /**
     * 【配置信息】根据条件分页查询
     * @param page
     * @param sysConfig
     * @return
     */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysConfig:getPage")
    public ResultInfo getPage(Page<SysConfig> page, SysConfig sysConfig) {
        IPage<SysConfig> iPage = sysConfigService.getPage(page, sysConfig);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
     * 【配置信息】根据条件查询
     * @param sysConfig
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(SysConfig sysConfig) {
        List<SysConfig> list = sysConfigService.getList(sysConfig);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【配置信息】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysConfig:edit","sysConfig:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysConfig sysConfig = sysConfigService.get(id);
        return ResultInfo.ok("获取对象成功", sysConfig);
    }

    /**
     * 【配置信息】提交(新增或修改)
     * @param sysConfig
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("sysConfig:save")
    public ResultInfo insert(SysConfig sysConfig) {
        if (StringUtils.isEmpty(sysConfig.getId())) { //新增
            sysConfigService.insert(sysConfig);
        } else {//修改
            sysConfigService.update(sysConfig);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
     * 【配置信息】删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sysConfig:delete")
    public ResultInfo delete(String id) {
        sysConfigService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【配置信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysConfigService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    //全局配置页面
    @RequiresPermissions("sysConfig:getPage")
    @RequestMapping("/getConfigMap")
    public ResultInfo getConfigMap(HttpServletRequest request) {
        List<SysConfig> configList = sysConfigService.getList(null);
        JSONObject configs = new JSONObject();
        for (SysConfig config : configList) {
            if(!"genTableConfigs".equals(config.getCode())){
                configs.put(config.getCode(), config.getVal());
            }

        }
        return ResultInfo.ok("获取配置成功",configs);
    }

    @RequiresPermissions("sysConfig:save")
    @RequestMapping("/editSub")
    public ResultInfo editSub(HttpServletRequest request,ConfigParams configParams) {
        for (SysConfig config : configParams.getConfigs()) {//移除移除空值数据
            if (StringUtils.isNotEmpty(config.getCode())) {
                SysConfig params = new SysConfig();
                params.setCode(config.getCode());
                SysConfig sysConfigF = sysConfigService.get(params);
                sysConfigF.setVal(config.getVal());
                sysConfigService.update(sysConfigF);
            }
        }
        sysCacheService.initConfigCache();
        return ResultInfo.ok("系统成功");
    }

    @RequestMapping(value = "/updateMenu")
    public ResultInfo updateMenu(HttpServletRequest request,String menuJson) {
        JSONObject jsonObject = JSONObject.fromObject(menuJson);
        OfficialApiUtils.delMenu();
        JSONObject menu = OfficialApiUtils.createMenu(jsonObject);
        return ResultInfo.ok("菜单同步成功!");
    }

    /**
     *【配置信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysConfig:export")
    public void exportFile(SysConfig sysConfig, HttpServletResponse response) {
        try {
            String fileName = "配置信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysConfig> list = sysConfigService.getList(sysConfig);
            new ExportExcel("配置信息", SysConfig.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【配置信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysConfig:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysConfig> list = ei.getDataList(SysConfig.class);
            for (SysConfig sysConfig : list) {
                try {
                    sysConfigService.insert(sysConfig);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条配置信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条配置信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入配置信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【配置信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysConfig:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "配置信息数据导入模板.xlsx";
            List<SysConfig> list = new ArrayList<>();
            new ExportExcel("配置信息数据", SysConfig.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

}



