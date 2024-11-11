package com.icss.sys.base.module.attach.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;

/**
 *【附件信息】实体对象
 */

@TableName("sys_attach")
public class SysAttach extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 文件名称 */
    private String fileName;
    /*** 文件大小 */
    private String fileSize;
    /*** 文件模块 */
    private String fileModule;
    /*** 上传类型 */
    private String fileType;
    /*** 文件后缀 */
    private String suffix;
    /*** 保存路径 */
    private String savePath;
    /*** 关联id */
    private String refId;
    /*** 文件备注 */
    private String remarks;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="文件名称",align=1, sort=1)
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    @ExcelField(title="文件大小",align=1, sort=2)
    public String getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    @ExcelField(title="文件模块",align=1, sort=3)
    public String getFileModule() {
        return this.fileModule;
    }
    public void setFileModule(String fileModule) {
        this.fileModule = fileModule;
    }
    @ExcelField(title="上传类型",align=1, sort=4)
    public String getFileType() {
        return this.fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    @ExcelField(title="文件后缀",align=1, sort=5)
    public String getSuffix() {
        return this.suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    @ExcelField(title="保存路径",align=1, sort=6)
    public String getSavePath() {
        return this.savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    @ExcelField(title="关联id",align=1, sort=7)
    public String getRefId() {
        return this.refId;
    }
    public void setRefId(String refId) {
        this.refId = refId;
    }
    @ExcelField(title="文件备注",align=1, sort=8)
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}