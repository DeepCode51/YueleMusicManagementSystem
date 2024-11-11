package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【歌手信息】实体对象
*/

@TableName("singer")
public class Singer extends BaseEntity {
    /*** 主键 */
    private String id;
    /*** 头像 */
    private String picture;
    /*** 姓名 */
    private String name;
    /*** 性别 */
    private Integer sex;
    /*** 性别 范围查询*/
    @TableField(exist = false)
    private Integer[] sexRange;
    /*** 地区 */
    private Integer regionArea;
    /*** 地区 范围查询*/
    @TableField(exist = false)
    private Integer[] regionAreaRange;
    /*** 简介 */
    private String introduction;
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPicture() {
        return this.picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getSex() {
        return this.sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer[] getSexRange(){
        return this.sexRange;
    }
    public void setSexRange(Integer[] sexRange){
        this.sexRange = sexRange;
    }
    public Integer getRegionArea() {
        return this.regionArea;
    }
    public void setRegionArea(Integer regionArea) {
        this.regionArea = regionArea;
    }
    public Integer[] getRegionAreaRange(){
        return this.regionAreaRange;
    }
    public void setRegionAreaRange(Integer[] regionAreaRange){
        this.regionAreaRange = regionAreaRange;
    }
    public String getIntroduction() {
        return this.introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}