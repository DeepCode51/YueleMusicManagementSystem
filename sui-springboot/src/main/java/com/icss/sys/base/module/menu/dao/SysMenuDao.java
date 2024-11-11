package com.icss.sys.base.module.menu.dao;

import com.icss.sys.base.module.menu.entity.SysMenu;
import com.icss.sys.base.module.role.dto.MenuRoleTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 【菜单信息】数据接口
 * SysMenuDao与SysMenuDaoMapper.xml进行接口映射
 * 用于扩展自定义sql方法
 */
@Repository
public interface SysMenuDao extends BaseMapper<SysMenu> {
    List<SysMenu> getMenuUserTree(@Param(value = "pid") String pid, @Param(value = "userId") String userId);
    List<MenuRoleTree> getMenuRoleTree(@Param(value = "pid") String pid, @Param(value = "roleId") String roleId);
    Set<SysMenu> findPermissionsByUserId(String userId);
}

