package com.icss.sys.base.module.extend.entity;

import com.icss.sys.utils.session.SessionUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基础类
 */
public class BaseEntity implements Serializable {
    protected String id;
    //创建人
    protected String createBy;
    //创建日期
    protected Date createDate;
    //修改人
    @JsonProperty("disable")
    protected String updateBy;
    //修改日期
    @JsonProperty("disable")
    protected Date updateDate;
    //删除标识（0：正常；1：删除）
    @JsonProperty("disable")
    protected Integer delFlag;
    //附件信息
    @TableField(exist = false)
    protected List<String> fileList;

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //新增方法前
    public void preInsert() {
        String userId = SessionUtils.getCurrentUserId();
        Date currentTime = new Date();
        this.createBy = userId;
        this.createDate = currentTime;
        this.updateBy = userId;
        this.updateDate = currentTime;

    }
    //修改方法前
    public void preUpdate(){
        String userId = SessionUtils.getCurrentUserId();
        this.updateBy = userId;
        this.updateDate = new Date();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
