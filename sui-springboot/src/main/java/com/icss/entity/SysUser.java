package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.module.office.entity.SysOffice;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
*【后台用户】实体对象
*/

@TableName("sys_user")
public class SysUser extends BaseEntity {
    /*** 编号 */
    private String id;
    /*** 用户头像 */
    private String photo;
    /*** 登录名 */
    private String loginName;
    /*** 密码 */
    @JsonProperty("disable")
    private String password;
    /*** 姓名 */
    private String userName;
    /*** 性别 */
    private Integer sex;
    /*** 用户编号 */
    private String userNo;
    /*** 用户地址 */
    private String address;
    /*** 用户备注 */
    private String remarks;
    /*** 身份证号 */
    private String idCard;
    /*** 是否锁定1：禁用 0：可用 */
    private Integer disabled;
    /*** 邮箱 */
    private String email;
    /*** 电话 */
    private String phone;
    /*** 机构id */
    private String officeId;
    /*** 所属机构 */
    @TableField(exist = false)
    private SysOffice office;
    /*** 角色编码 */
    @TableField(exist = false)
    private String roleCode;
    /*** 所属角色 */
    @TableField(exist = false)
    //初始化避免传值null判断
    private List<SysRole> roleList = new ArrayList<>();

    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="用户头像",align=1, sort=1)
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    @ExcelField(title="登录名",align=1, sort=2)
    public String getLoginName() {
        return this.loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @ExcelField(title="密码",align=1, sort=3)
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @ExcelField(title="姓名",align=1, sort=4)
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @ExcelField(title="性别",dictType="sys_sex",align=1, sort=5)
    public Integer getSex() {
        return this.sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    @ExcelField(title="手机号",align=1, sort=6)
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @ExcelField(title="邮箱",align=1, sort=7)
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @ExcelField(title="身份证号",align=1, sort=8)
    public String getIdCard() {
        return this.idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    @ExcelField(title="归属部门",align=1, sort=9)
    public String getOfficeId() {
        return this.officeId;
    }
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
    @ExcelField(title="工号",align=1, sort=10)
    public String getUserNo() {
        return this.userNo;
    }
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
    @ExcelField(title="是否禁用",dictType="sys_yes_no",align=1, sort=11)
    public Integer getDisabled() {
        return this.disabled;
    }
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
    @ExcelField(title="地址",align=1, sort=12)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @ExcelField(title="备注",align=1, sort=13)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public SysOffice getOffice() {
        return office;
    }

    public void setOffice(SysOffice office) {
        this.office = office;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }
}