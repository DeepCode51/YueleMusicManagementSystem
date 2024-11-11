package ${packageName}.service;

import ${packageName}.dao.${className}Dao;
import ${packageName}.entity.${className};
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.admin.sys.utils.admin.StringUtils;
import com.admin.sys.utils.admin.ObjectUtils;
import com.admin.sys.extend.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

//${comments}接口类
@Service
public class ${className}Service extends BaseService {

    @Autowired
    private ${className}Dao ${className?uncap_first}Dao;

    //【${comments}】设置查询条件
    private LambdaQueryWrapper<${className}> get${className}QueryCondition(${className} ${className?uncap_first}) {
        LambdaQueryWrapper<${className}> lambdaQuery = this.getBaseQueryCondition(${className?uncap_first});
        //根据创建时间排序
        lambdaQuery.orderByDesc(${className}::getCreateDate);
        if (ObjectUtils.isNotNull(${className?uncap_first})) {
<#list attrs as attr>
   <#if attr.columnFieldName!='createBy'&&attr.columnFieldName!='createDate'&&attr.columnFieldName!='updateBy'&&attr.columnFieldName!='updateDate'&&attr.columnFieldName!='delFlag'>
        <#if attr.inputType == 'text'>
            <#if attr.queryType == 'like'>
            //【${attr.columnDesc}】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
            </#if>
            <#if attr.queryType == 'equal'>
            //【${attr.columnDesc}】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
            </#if>
        </#if>
            <#if attr.inputType == 'select'>
            //【${attr.columnDesc}】范围查询
            lambdaQuery.in(ObjectUtils.isNotNull(${className?uncap_first}.get${attr.columnFieldName?cap_first}Range()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}Range());
            //【${attr.columnDesc}】精确查询
            lambdaQuery.eq(ObjectUtils.isNotNull(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
            </#if>
            <#if attr.inputType == 'office'>
            //【${attr.columnDesc}】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
            </#if>
           <#if attr.inputType == 'associate'>
            //【${attr.columnDesc}】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
           </#if>
        <#if attr.inputType == 'date'>
            //【${attr.columnDesc}】精确查询
            lambdaQuery.eq(ObjectUtils.isNotNull(${className?uncap_first}.get${attr.columnFieldName?cap_first}()), ${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}());
            //【${attr.columnDesc}】范围查询
            if (ObjectUtils.isNotNull(${className?uncap_first}.get${attr.columnFieldName?cap_first}Range()) && ${className?uncap_first}.get${attr.columnFieldName?cap_first}Range().size() > 0) {
                lambdaQuery.between(${className}::get${attr.columnFieldName?cap_first}, ${className?uncap_first}.get${attr.columnFieldName?cap_first}Range().get(0), ${className?uncap_first}.get${attr.columnFieldName?cap_first}Range().get(1));
            }
        </#if>
   </#if>
</#list>
        }
        return lambdaQuery;
    }

    //【${comments}】分页查询
    public IPage<${className}> getPage(Page<${className}> page, ${className} ${className?uncap_first}) {
        LambdaQueryWrapper<${className}> lambdaQuery = get${className}QueryCondition(${className?uncap_first});
        return ${className?uncap_first}Dao.selectPage(page, lambdaQuery);
    }
    
    //【${comments}】查询列表
    public List<${className}> getList(${className} ${className?uncap_first}) {
        LambdaQueryWrapper<${className}> lambdaQuery = get${className}QueryCondition(${className?uncap_first});
        return ${className?uncap_first}Dao.selectList(lambdaQuery);
    }
    
    //【${comments}】根据id查询
    public ${className} get(String id) {
        return ${className?uncap_first}Dao.selectById(id);
    }

    //【${comments}】根据对象查询
    public ${className} get(${className} ${className?uncap_first}) {
    LambdaQueryWrapper<${className}> lambdaQuery = get${className}QueryCondition(${className?uncap_first});
        return ${className?uncap_first}Dao.selectOne(lambdaQuery);
    }
    
    //【${comments}】新增
    public int insert(${className} ${className?uncap_first}) {
        this.preInsert(${className?uncap_first});
        return ${className?uncap_first}Dao.insert(${className?uncap_first});
    }
    
    //【${comments}】修改
    public int update(${className} ${className?uncap_first}) {
        this.preUpdate(${className?uncap_first});
        return ${className?uncap_first}Dao.updateById(${className?uncap_first});
    }
    
    //【${comments}】删除
    public int delete(String id) {
        return ${className?uncap_first}Dao.deleteById(id);
    }

    //【${comments}】批量删除
    public int delAll(String[] ids) {
        return ${className?uncap_first}Dao.deleteBatchIds(Arrays.asList(ids));
    }

}