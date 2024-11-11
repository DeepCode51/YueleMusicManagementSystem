package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
*【歌单关联信息】实体对象
*/

@TableName("list_song")
public class ListSong extends BaseEntity {
    /*** 主键 */
    private String id;
    /*** 所属歌单 */
    private String songSheetId;
    /*** 所属歌曲 */
    private String songId;
    @TableField(exist = false)
    private String[] songIds;
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSongSheetId() {
        return this.songSheetId;
    }
    public void setSongSheetId(String songSheetId) {
        this.songSheetId = songSheetId;
    }
    public String getSongId() {
        return this.songId;
    }
    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String[] getSongIds() {
        return songIds;
    }

    public void setSongIds(String[] songIds) {
        this.songIds = songIds;
    }
}