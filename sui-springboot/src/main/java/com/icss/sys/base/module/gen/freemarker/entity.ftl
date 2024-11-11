package ${packageName}.entity;

import com.admin.sys.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.admin.sys.base.annotation.ExcelField;
import java.util.Date;
import java.util.List;

/**
*【${comments}】实体对象
*/

@TableName("${tableName}")
public class ${className} extends BaseEntity {
<#list attrs as attr>
    /*** ${attr.columnDesc} */
    private ${attr.columnFieldType} ${attr.columnFieldName};
    <#if attr.inputType == 'date' && attr.queryType == 'between'>
    /*** ${attr.columnDesc} 范围查询*/
    @TableField(exist = false)
    private List<String> ${attr.columnFieldName}Range;
    </#if>
    <#if attr.inputType == 'select'>
    /*** ${attr.columnDesc} 范围查询*/
    @TableField(exist = false)
    private Integer[] ${attr.columnFieldName}Range;
    </#if>
</#list>
<#list attrs as attr>
    <#if attr.inputType == 'date'>
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    </#if>
    <#if isPort=='true' && attr.isPk != 1>
    @ExcelField(title="${attr.columnDesc}",<#if attr.inputType=='select'>dictType="${attr.dictType}",</#if><#if attr.inputType=='office'>dictType="office",</#if>align=1, sort=${attr_index})
    </#if>
    public ${attr.columnFieldType} get${attr.columnFieldName?cap_first}() {
        return this.${attr.columnFieldName};
    }
    public void set${attr.columnFieldName?cap_first}(${attr.columnFieldType} ${attr.columnFieldName}) {
        this.${attr.columnFieldName} = ${attr.columnFieldName};
    }
    <#if attr.inputType == 'date' && attr.queryType == 'between'>
    public List<String> get${attr.columnFieldName?cap_first}Range(){
        return this.${attr.columnFieldName}Range;
    }
    public void set${attr.columnFieldName?cap_first}Range(List<String> ${attr.columnFieldName}Range){
        this.${attr.columnFieldName}Range = ${attr.columnFieldName}Range;
    }
    </#if>
    <#if attr.inputType == 'select'>
    public Integer[] get${attr.columnFieldName?cap_first}Range(){
        return this.${attr.columnFieldName}Range;
    }
    public void set${attr.columnFieldName?cap_first}Range(Integer[] ${attr.columnFieldName}Range){
        this.${attr.columnFieldName}Range = ${attr.columnFieldName}Range;
    }
    </#if>
</#list>
}