package com.icss.sys.base.module.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.icss.sys.base.annotation.ExcelField;

import java.util.Date;

/**
*【日志信息】实体对象
*/

@TableName("sys_log")
public class SysLog extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 用户名称 */
    private String loginName;
    /*** 用户ID */
    private String userId;
    /*** 系统类型 */
    private Integer systemType;
    /*** 模块名称 */
    private String moduleName;
    /*** 方法类型 */
    private String method;
    /*** 操作日期 */
    private Date operationDate;
    /*** 请求URI */
    private String requestUri;
    /*** 请求端口 */
    private String remotePort;
    /*** 本地主机 */
    private String localName;
    /*** 本地地址 */
    private String localAddr;
    /*** 远程主机 */
    private String remoteHost;
    /*** 远程地址 */
    private String remoteAddr;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="用户名称",align=1, sort=1)
    public String getLoginName() {
        return this.loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @ExcelField(title="用户ID",align=1, sort=2)
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @ExcelField(title="系统类型",dictType="system_type",align=1, sort=3)
    public Integer getSystemType() {
        return this.systemType;
    }
    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }
    @ExcelField(title="模块名称",align=1, sort=4)
    public String getModuleName() {
        return this.moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    @ExcelField(title="方法类型",align=1, sort=5)
    public String getMethod() {
        return this.method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ExcelField(title="操作日期",align=1, sort=6)
    public Date getOperationDate() {
        return this.operationDate;
    }
    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }
    @ExcelField(title="请求URI",align=1, sort=7)
    public String getRequestUri() {
        return this.requestUri;
    }
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }
    @ExcelField(title="请求端口",align=1, sort=8)
    public String getRemotePort() {
        return this.remotePort;
    }
    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }
    @ExcelField(title="本地主机",align=1, sort=9)
    public String getLocalName() {
        return this.localName;
    }
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    @ExcelField(title="本地地址",align=1, sort=10)
    public String getLocalAddr() {
        return this.localAddr;
    }
    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }
    @ExcelField(title="远程主机",align=1, sort=11)
    public String getRemoteHost() {
        return this.remoteHost;
    }
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }
    @ExcelField(title="远程地址",align=1, sort=12)
    public String getRemoteAddr() {
        return this.remoteAddr;
    }
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

}