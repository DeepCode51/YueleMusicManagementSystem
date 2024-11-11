package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【歌单信息】实体对象
*/

@TableName("song_sheet")
public class SongSheet extends BaseEntity {
    /*** 主键 */
    private String id;
    /*** 歌单图片 */
    private String picture;
    /*** 歌单标题 */
    private String title;
    /*** 歌单分类 */
    private String categoryId;
    /*** 歌单简介 */
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
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getIntroduction() {
        return this.introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}