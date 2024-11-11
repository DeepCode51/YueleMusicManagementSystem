package com.icss.sys.base.module.roleMenu.service;

import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.roleMenu.dao.SysRoleMenuDao;
import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//角色-菜单接口类
@Service
public class SysRoleMenuService extends BaseService {

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    //【角色-菜单】设置查询条件
    private LambdaQueryWrapper<SysRoleMenu> getMenuQueryCondition(SysRoleMenu sysRoleMenu) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQuery = Wrappers.lambdaQuery();
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysRoleMenu::getId);
        if (ObjectUtils.isNotNull(sysRoleMenu)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRoleMenu.getId()), SysRoleMenu::getId, sysRoleMenu.getId());
            //【角色编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRoleMenu.getRoleId()), SysRoleMenu::getRoleId, sysRoleMenu.getRoleId());
            //【菜单编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRoleMenu.getMenuId()), SysRoleMenu::getMenuId, sysRoleMenu.getMenuId());
        }
        return lambdaQuery;
    }


    //【角色-菜单】分页查询
    public IPage<SysRoleMenu> getPage(Page<SysRoleMenu> page, SysRoleMenu sysRoleMenu) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQuery = getMenuQueryCondition(sysRoleMenu);
        return sysRoleMenuDao.selectPage(page, lambdaQuery);
    }
    
    //【角色-菜单】查询列表
    public List<SysRoleMenu> getList(SysRoleMenu sysRoleMenu) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQuery = getMenuQueryCondition(sysRoleMenu);
        return sysRoleMenuDao.selectList(lambdaQuery);
    }
    
    //【角色-菜单】根据id查询
    public SysRoleMenu get(String id) {
        return sysRoleMenuDao.selectById(id);
    }

    //【角色-菜单】根据对象查询
    public SysRoleMenu get(SysRoleMenu sysRoleMenu) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQuery = getMenuQueryCondition(sysRoleMenu);
        return sysRoleMenuDao.selectOne(lambdaQuery);
    }

    //【角色-菜单】新增
    public int insert(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuDao.insert(sysRoleMenu);
    }
    
    //【角色-菜单】修改
    public int update(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuDao.updateById(sysRoleMenu);
    }
    
    //【角色-菜单】删除
    public int delete(String id) {
        return sysRoleMenuDao.deleteById(id);
    }

}