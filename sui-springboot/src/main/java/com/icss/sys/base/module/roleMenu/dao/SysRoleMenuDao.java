package com.icss.sys.base.module.roleMenu.dao;

import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* 【角色-菜单】数据接口
* SysRoleMenuDao与SysRoleMenuDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenu> {

}

