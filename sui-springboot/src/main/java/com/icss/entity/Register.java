package com.icss.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
*【前台用户】实体对象
*/

@TableName("register")
public class Register extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 头像 */
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
    /*** 工号 */
    private String userNo;
    /*** 手机号 */
    private String phone;
    /*** 身份证号 */
    private String idCard;
    /*** 邮箱 */
    private String email;
    /*** 用户类型 */
    private Integer userType;
    /*** 详细地址 */
    private String address;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="头像",align=1, sort=1)
    public String getPhoto() {
        return photo;
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
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @ExcelField(title="工号",align=1, sort=6)
    public String getUserNo() {
        return this.userNo;
    }
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
    @ExcelField(title="手机号",align=1, sort=7)
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @ExcelField(title="身份证号",align=1, sort=8)
    public String getIdCard() {
        return this.idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    @ExcelField(title="邮箱",align=1, sort=9)
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @ExcelField(title="用户类型",dictType="user_type",align=1, sort=10)
    public Integer getUserType() {
        return this.userType;
    }
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    @ExcelField(title="详细地址",align=1, sort=12)
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}