package com.icss.sys.base.module.role.dao;

import com.icss.sys.base.module.role.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 【角色信息】数据接口
* SysRoleDao与SysRoleDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysRoleDao extends BaseMapper<SysRole> {
    int deleteMenusByRoleId(String roleId);
    //delFlag==true 查询出为0的数据
    List<SysRole> findCheckedRolesByUserId(String userId, boolean delFlag);

    List<SysRole> findRolesListByUserId(@Param(value = "userId") String userId);
}

