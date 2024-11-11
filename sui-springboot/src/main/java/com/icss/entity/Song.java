package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【歌曲信息】实体对象
*/

@TableName("song")
public class Song extends BaseEntity {
    /*** 主键 */
    private String id;
    /*** 歌曲图片 */
    private String picture;
    /*** 歌曲名称 */
    private String name;
    /*** 歌手姓名 */
    private String singerId;
    /*** 歌手姓名 */
    @TableField(exist = false)
    private String singerName;
    /*** 歌曲地址 */
    private String url;
    /*** 歌曲歌词 */
    private String lyric;
    /*** 歌曲简介 */
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
    public String getSingerId() {
        return this.singerId;
    }
    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }
    public String getUrl() {
        return this.url;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getLyric() {
        return this.lyric;
    }
    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
    public String getIntroduction() {
        return this.introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}