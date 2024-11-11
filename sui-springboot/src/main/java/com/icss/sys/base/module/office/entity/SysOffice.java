package com.icss.sys.base.module.office.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.icss.sys.base.annotation.ExcelField;

import java.util.List;

/**
*【机构信息】实体对象
*/

@TableName("sys_office")
public class SysOffice extends BaseEntity {
    /*** 编号 */
    private String id;
    /*** 父级编号 */
    private String pid;
    /*** 名称 */
    private String name;
    /*** 编码 */
    private String code;
    /*** 类型 */
    private Integer type;
    /*** 负责人 */
    private String leader;
    /*** 邮箱 */
    private String email;
    /*** 手机号 */
    private String phone;
    /*** 排序 */
    private Integer sort;
    /*** 备注信息 */
    private String remarks;
    @TableField(exist = false)
    private List<SysOffice> children;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="父级编号",align=1, sort=1)
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    @ExcelField(title="名称",align=1, sort=2)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @ExcelField(title="编码",align=1, sort=3)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @ExcelField(title="类型",dictType="office_type",align=1, sort=4)
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @ExcelField(title="负责人",align=1, sort=5)
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
    @ExcelField(title="邮箱",align=1, sort=6)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @ExcelField(title="联系电话",align=1, sort=7)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @ExcelField(title="排序",align=1, sort=8)
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @ExcelField(title="备注信息",align=1, sort=9)
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public List<SysOffice> getChildren() {
        return children;
    }
    public void setChildren(List<SysOffice> children) {
        this.children = children;
    }
}