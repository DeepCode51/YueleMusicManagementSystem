package com.icss.entity;

import com.icss.sys.base.annotation.ExcelField;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【播放清单信息】实体对象
*/

@TableName("play_list")
public class PlayList extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 所属用户 */
    private String userId;
    /*** 所属音乐 */
    @ExcelField(title="歌曲名称",align=1, sort=1)
    private String songId;
    /*** 歌曲图片 */
    @TableField(exist = false)
    private String picture;
    /*** 歌手名字 */
    @TableField(exist = false)
    private String singerName;
    /*** 歌曲名称 */
    @TableField(exist = false)
    private String songName;
    //查询类型
    @TableField(exist = false)
    private String searchType;
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSongId() {
        return this.songId;
    }
    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}