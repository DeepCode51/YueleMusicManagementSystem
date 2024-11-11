package com.icss.sys.base.module.userRole.dao;

import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* 【用户-角色】数据接口
* SysUserRoleDao与SysUserRoleDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {

}

