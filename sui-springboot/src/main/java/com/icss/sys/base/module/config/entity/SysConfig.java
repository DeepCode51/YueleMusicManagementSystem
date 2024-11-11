package com.icss.sys.base.module.config.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;

/**
*【系统配置信息】实体对象
*/

@TableName("sys_config")
public class SysConfig extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 配置名称 */
    private String name;
    /*** 配置编码 */
    private String code;
    /*** 配置取值 */
    private String val;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="配置名称",align=1, sort=1)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @ExcelField(title="配置编码",align=1, sort=2)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @ExcelField(title="配置取值",align=1, sort=3)
    public String getVal() {
        return this.val;
    }
    public void setVal(String val) {
        this.val = val;
    }
}