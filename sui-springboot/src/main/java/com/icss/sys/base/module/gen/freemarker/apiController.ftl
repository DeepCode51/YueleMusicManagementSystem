package com.front;

import com.admin.module.${className?uncap_first}.service.${className}Service;
import com.admin.module.${className?uncap_first}.entity.${className};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.admin.sys.utils.admin.StringUtils;
import javax.servlet.http.HttpServletRequest;
import com.admin.sys.extend.web.BaseController;
import com.admin.sys.base.entity.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
*【${comments}】管理
*/
@RestController
@RequestMapping("/api/${className?uncap_first}")
public class Api${className}Controller extends BaseController {

    @Autowired
    private ${className}Service ${className?uncap_first}Service;

    /**
     *【${comments}】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<${className}> page, ${className} ${className?uncap_first}) {
        IPage<${className}> iPage = ${className?uncap_first}Service.getPage(page, ${className?uncap_first});
        return ResultInfo.ok("获取分页成功",iPage);
    }

    /**
    * 【${comments}】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(${className} ${className?uncap_first}) {
        List<${className}> list = ${className?uncap_first}Service.getList(${className?uncap_first});
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【${comments}】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(${className} ${className?uncap_first}) {
        ${className} entity = ${className?uncap_first}Service.get(${className?uncap_first});
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【${comments}】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,${className} ${className?uncap_first}){
        try {
            if(StringUtils.isEmpty(${className?uncap_first}.getId())){
                ${className?uncap_first}Service.insert(${className?uncap_first});//新增
                return ResultInfo.ok("保存成功！");
            }else{
                ${className?uncap_first}Service.update(${className?uncap_first});//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【${comments}】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, ${className} ${className?uncap_first}) {
        ${className?uncap_first}Service.delete(${className?uncap_first}.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【${comments}】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        ${className?uncap_first}Service.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



