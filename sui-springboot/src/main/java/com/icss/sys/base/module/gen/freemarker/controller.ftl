package ${packageName}.web;

import ${packageName}.service.${className}Service;
import ${packageName}.entity.${className};
import com.admin.sys.extend.web.BaseController;
import com.admin.sys.base.entity.ResultInfo;
import com.admin.sys.utils.admin.StringUtils;
import com.admin.sys.base.entity.DateUtils;
import com.admin.sys.utils.excel.ExportExcel;
import com.admin.sys.utils.excel.ImportExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*【${comments}】页面接口
*/
@RestController
@RequestMapping("/admin/${className?uncap_first}")
public class ${className}Controller extends BaseController {

    @Autowired
    private ${className}Service ${className?uncap_first}Service;

    /**
    * 【${comments}】根据条件分页查询
    * @param page
    * @param ${className?uncap_first}
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<${className}> page, ${className} ${className?uncap_first}) {
        IPage<${className}> iPage = ${className?uncap_first}Service.getPage(page, ${className?uncap_first});
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【${comments}】根据条件查询
    * @param ${className?uncap_first}
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(${className} ${className?uncap_first}) {
        List<${className}> list = ${className?uncap_first}Service.getList(${className?uncap_first});
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【${comments}】根据对象查询
    * @param ${className?uncap_first}
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(${className} ${className?uncap_first}) {
        ${className} entity = ${className?uncap_first}Service.get(${className?uncap_first});
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【${comments}】提交(新增或修改)
    * @param ${className?uncap_first}
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(${className} ${className?uncap_first}) {
        if (StringUtils.isEmpty(${className?uncap_first}.getId())) { //新增
            ${className?uncap_first}Service.insert(${className?uncap_first});
        } else {//修改
            ${className?uncap_first}Service.update(${className?uncap_first});
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【${comments}】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        ${className?uncap_first}Service.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【${comments}】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        ${className?uncap_first}Service.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }


<#if isPort=='true'>
    /**
    *【${comments}】导出
    */
    @RequestMapping(value = "/export")
    public void exportFile(${className} ${className?uncap_first}, HttpServletResponse response) {
        try {
            String fileName = "${comments}" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<${className}> list = ${className?uncap_first}Service.getList(${className?uncap_first});
            new ExportExcel("${comments}", ${className}.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *【${comments}】导入
    */
    @RequestMapping(value = "/import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<${className}> list = ei.getDataList(${className}.class);
            for (${className} ${className?uncap_first} : list) {
                try {
                    ${className?uncap_first}Service.insert(${className?uncap_first});
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条${comments}记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条${comments}记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入${comments}失败！失败信息：" + e.getMessage());
        }
    }

    /**
    *【${comments}】模板下载
    */
    @RequestMapping(value = "/import/template")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "${comments}数据导入模板.xlsx";
            List<${className}> list = new ArrayList<>();
            new ExportExcel("${comments}数据", ${className}.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }
</#if>

}



