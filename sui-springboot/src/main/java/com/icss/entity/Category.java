package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【类别信息】实体对象
*/

@TableName("category")
public class Category extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 类别名称 */
    private String name;
    /*** 类别描述 */
    private String content;
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}