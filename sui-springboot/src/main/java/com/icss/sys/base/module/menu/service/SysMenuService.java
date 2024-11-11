package com.icss.sys.base.module.menu.service;

import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.menu.dao.SysMenuDao;
import com.icss.sys.base.module.menu.entity.SysMenu;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.icss.sys.base.module.roleMenu.service.SysRoleMenuService;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//菜单信息接口类
@Service
public class SysMenuService extends BaseService {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //【菜单信息】设置查询条件
    private LambdaQueryWrapper<SysMenu> getMenuQueryCondition(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> lambdaQuery = this.getBaseQueryCondition(sysMenu);
        //根据创建时间排序
        lambdaQuery.orderByAsc(SysMenu::getSort).orderByAsc(SysMenu::getName);
        //【是否显示】精确查询
        lambdaQuery.eq(StringUtils.isNotNull(sysMenu.getIsShow()), SysMenu::getIsShow, sysMenu.getIsShow());
        //【排序】精确查询
        lambdaQuery.eq(StringUtils.isNotEmpty(sysMenu.getSort()), SysMenu::getSort, sysMenu.getSort());
        //【权限标识】精确查询
        lambdaQuery.like(StringUtils.isNotEmpty(sysMenu.getPermission()), SysMenu::getPermission, sysMenu.getPermission());
        //【链接】精确查询
        lambdaQuery.eq(StringUtils.isNotEmpty(sysMenu.getHref()), SysMenu::getHref, sysMenu.getHref());
        //【父级编号】精确查询
        lambdaQuery.eq(StringUtils.isNotEmpty(sysMenu.getPid()), SysMenu::getPid, sysMenu.getPid());
        //【图标】精确查询
        lambdaQuery.eq(StringUtils.isNotEmpty(sysMenu.getIcon()), SysMenu::getIcon, sysMenu.getIcon());
        //【名称】精确查询
        lambdaQuery.like(StringUtils.isNotEmpty(sysMenu.getName()), SysMenu::getName, sysMenu.getName());
        //【备注信息】精确查询
        lambdaQuery.eq(StringUtils.isNotEmpty(sysMenu.getRemarks()), SysMenu::getRemarks, sysMenu.getRemarks());
        return lambdaQuery;
    }


    //【菜单信息】分页查询
    public IPage<SysMenu> getPage(Page<SysMenu> page, SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> lambdaQuery = getMenuQueryCondition(sysMenu);
        return sysMenuDao.selectPage(page, lambdaQuery);
    }
    
    //【菜单信息】查询列表
    public List<SysMenu> getList(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> lambdaQuery = getMenuQueryCondition(sysMenu);
        return sysMenuDao.selectList(lambdaQuery);
    }
    
    //【菜单信息】根据id查询
    public SysMenu get(String id) {
        return sysMenuDao.selectById(id);
    }
    
    //【菜单信息】新增
    public int insert(SysMenu sysMenu) {
        sysMenu.preInsert();
        int insert = sysMenuDao.insert(sysMenu);
        this.insertRoleMenu(sysMenu.getId());
        return insert;
    }

    //设置管理员权限
    public void insertRoleMenu(String menuId){
        //管理员免菜单配置
        SysRole devParams = new SysRole();
        devParams.setRoleCode("devRole");
        SysRole devRole = sysRoleService.get(devParams);
        SysRoleMenu devRoleMenu = new SysRoleMenu();
        devRoleMenu.setMenuId(menuId);
        devRoleMenu.setRoleId(devRole.getId());
        sysRoleMenuService.insert(devRoleMenu);
        SysRole adminParams = new SysRole();
        adminParams.setRoleCode("adminRole");
        SysRole adminRole = sysRoleService.get(adminParams);
        SysRoleMenu adminRoleMenu = new SysRoleMenu();
        adminRoleMenu.setMenuId(menuId);
        adminRoleMenu.setRoleId(adminRole.getId());
        sysRoleMenuService.insert(adminRoleMenu);
    }

    //【菜单信息】修改
    public int update(SysMenu sysMenu) {
        sysMenu.preUpdate();
        return sysMenuDao.updateById(sysMenu);
    }
    
    //【菜单信息】删除
    public int delete(String id) {
        SysRoleMenu params = new SysRoleMenu();
        params.setMenuId(id);//删除角色-菜单数据
        List<SysRoleMenu> list = sysRoleMenuService.getList(params);
        for (SysRoleMenu sysRoleMenu : list) {
            sysRoleMenuService.delete(sysRoleMenu.getId());
        }
        return sysMenuDao.deleteById(id);
    }

    //【菜单信息】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            SysRoleMenu params = new SysRoleMenu();
            params.setMenuId(id);//删除角色-菜单数据
            List<SysRoleMenu> list = sysRoleMenuService.getList(params);
            for (SysRoleMenu sysRoleMenu : list) {
                sysRoleMenuService.delete(sysRoleMenu.getId());
            }
        }
        return sysMenuDao.deleteBatchIds(Arrays.asList(ids));
    }

    //获取当前用户菜单树
    public List<SysMenu> getMenuUserTree(String pid, String userId) {
        List<SysMenu> menuUserTree = sysMenuDao.getMenuUserTree(pid, userId);
        return menuUserTree;
    }

    //菜单数据转换成路由数据
    public List<JSONObject> menuFormatRoute(List<JSONObject> userRoutes, List<SysMenu> userMenus) {
        userMenus.forEach(item -> {
            if (item.getIsShow()==1 && StringUtils.isNotEmpty(item.getHref())) {//菜单地址
                String[] split = item.getComponent().split("/");
                JSONObject json = new JSONObject();
                json.put("name", item.getComponent().split("/")[split.length - 1].split("\\.")[0]);
                json.put("path", item.getHref());
                json.put("title", item.getName());
                json.put("component",item.getComponent());
                userRoutes.add(json);
            }
            if (item.getChildren() != null && item.getChildren().size() > 0) {//递归
                menuFormatRoute(userRoutes, item.getChildren());
            }
        });
        return userRoutes;
    }

    //菜单树节点递归
    public List<SysMenu> getMenuChildren(List<SysMenu> allList) {
        allList.forEach(item -> {
            SysMenu entity = new SysMenu();
            entity.setPid(item.getId());
            List<SysMenu> children = this.getList(entity);
            if(children.size()>0){
                item.setChildren(children);
            }
            if (item.getChildren() != null && item.getChildren().size() > 0) {//递归
                getMenuChildren(item.getChildren());
            }
        });
        return allList;
    }
}