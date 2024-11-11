package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
*【通知信息】实体对象
*/

@TableName("notice")
public class Notice extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 标题图片 */
    private String picture;
    /*** 消息标题 */
    private String title;
    /*** 通知类型 */
    private Integer noticeType;
    /*** 通知类型 范围查询*/
    @TableField(exist = false)
    private List<Integer> noticeTypeRange;
    /*** 发布时间 */
    private Date publishTime;
    /*** 发布时间 范围查询*/
    @TableField(exist = false)
    private List<String> publishTimeRange;
    /*** 消息内容 */
    private String content;
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
    public Integer getNoticeType() {
        return this.noticeType;
    }
    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
    public List<Integer> getNoticeTypeRange(){
        return this.noticeTypeRange;
    }
    public void setNoticeTypeRange(List<Integer> noticeTypeRange){
        this.noticeTypeRange = noticeTypeRange;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public Date getPublishTime() {
        return this.publishTime;
    }
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    public List<String> getPublishTimeRange(){
        return this.publishTimeRange;
    }
    public void setPublishTimeRange(List<String> publishTimeRange){
        this.publishTimeRange = publishTimeRange;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}