package com.icss.sys.base.module.role.service;

import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.menu.dao.SysMenuDao;
import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.icss.sys.base.module.roleMenu.service.SysRoleMenuService;
import com.icss.sys.base.module.role.dao.SysRoleDao;
import com.icss.sys.base.module.role.dto.MenuRoleParams;
import com.icss.sys.base.module.role.dto.MenuRoleTree;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.icss.sys.base.module.userRole.service.SysUserRoleService;
import com.icss.sys.utils.admin.IdGen;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//角色信息接口类
@Service
public class SysRoleService extends BaseService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleService sysRoleService;

    //【角色信息】设置查询条件
    private LambdaQueryWrapper<SysRole> getMenuQueryCondition(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> lambdaQuery = this.getBaseQueryCondition(sysRole);
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysRole::getUpdateDate);
        if (ObjectUtils.isNotNull(sysRole)) {
            //【备注信息】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRole.getRemarks()), SysRole::getRemarks, sysRole.getRemarks());
            //【角色编码】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRole.getRoleCode()), SysRole::getRoleCode, sysRole.getRoleCode());
            //【角色名称】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysRole.getRoleName()), SysRole::getRoleName, sysRole.getRoleName());
        }
        return lambdaQuery;
    }


    //【角色信息】分页查询
    public IPage<SysRole> getPage(Page<SysRole> page, SysRole sysRole) {
        sysRole.setDelFlag(0);
        LambdaQueryWrapper<SysRole> lambdaQuery = getMenuQueryCondition(sysRole);
        return sysRoleDao.selectPage(page, lambdaQuery);
    }
    
    //【角色信息】查询列表
    public List<SysRole> getList(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> lambdaQuery = getMenuQueryCondition(sysRole);
        return sysRoleDao.selectList(lambdaQuery);
    }
    
    //【角色信息】根据id查询
    public SysRole get(String id) {
        return sysRoleDao.selectById(id);
    }

    //【角色信息】根据对象查询
    public SysRole get(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> lambdaQuery = getMenuQueryCondition(sysRole);
        return sysRoleDao.selectOne(lambdaQuery);
    }

    //【角色信息】新增
    public int insert(SysRole sysRole) {
        sysRole.preInsert();
        return sysRoleDao.insert(sysRole);
    }
    
    //【角色信息】修改
    public int update(SysRole sysRole) {
        sysRole.preUpdate();
        return sysRoleDao.updateById(sysRole);
    }
    
    //【角色信息】删除
    public int delete(String id) {
        //删除角色时，删除用户-角色数据
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(id);
        List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
        for (SysUserRole userRole : list) {
            sysUserRoleService.delete(userRole.getId());
            //删除该角色下的角色-菜单数据
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(userRole.getId());
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getList(sysRoleMenu);
            for (SysRoleMenu roleMenu : sysRoleMenuList) {
                sysRoleMenuService.delete(roleMenu.getId());
            }
        }
        return sysRoleDao.deleteById(id);
    }

    //【角色信息】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            //删除角色时，删除用户-角色数据
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(id);
            List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
            for (SysUserRole userRole : list) {
                sysUserRoleService.delete(userRole.getId());
                //删除该角色下的角色-菜单数据
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(userRole.getId());
                List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getList(sysRoleMenu);
                for (SysRoleMenu roleMenu : sysRoleMenuList) {
                    sysRoleMenuService.delete(roleMenu.getId());
                }
            }
        }
        return sysMenuDao.deleteBatchIds(Arrays.asList(ids));
    }

    public List<MenuRoleTree> getMenuRoleTree(String pid, String roleId) {
        return sysMenuDao.getMenuRoleTree(pid,roleId);
    }

    public void updateMenuRoleTree(MenuRoleParams menuRoleParams) {
        String roleId = menuRoleParams.getRoleId();
        List<String> menuIds = menuRoleParams.getMenuIds();
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setRoleId(roleId);
        sysRoleDao.deleteMenusByRoleId(roleId);
        //更新的权限
        if (menuIds != null && menuIds.size() > 0) {
            for (String menuId : menuIds) {
                SysRoleMenu entity = new SysRoleMenu();
                entity.setId(IdGen.primaryKey());
                entity.setRoleId(roleId);
                entity.setMenuId(menuId);
                sysRoleMenuService.insert(entity);
            }
        }
    }
    //递归获取菜单树数据
    public List<MenuRoleTree> getChrien(List<MenuRoleTree> listTree, String roleId) {
        for (MenuRoleTree menuRoleTree : listTree) {
            List<MenuRoleTree> treeChild = sysRoleService.getMenuRoleTree(menuRoleTree.getId(), roleId); //查询对应菜单栏下的资源
            if (treeChild.size() > 0) {
                menuRoleTree.setChecked(false);
                menuRoleTree.setChildren(treeChild);
            }
            getChrien(treeChild, roleId);
        }
        return listTree;
    }
    //查询用户角色
    public List<SysRole> findRolesListByUserId(String userId) {
        return sysRoleDao.findRolesListByUserId(userId);
    }

    //查询所有角色当前用户勾选:用户页面用
    public List<SysRole> findCheckedRolesByUserId(String userId) {
        return sysRoleDao.findCheckedRolesByUserId(userId,true);
    }
}