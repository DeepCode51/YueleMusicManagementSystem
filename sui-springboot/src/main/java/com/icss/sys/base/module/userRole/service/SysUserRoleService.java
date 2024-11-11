package com.icss.sys.base.module.userRole.service;

import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.userRole.dao.SysUserRoleDao;
import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.icss.sys.utils.admin.IdGen;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//用户-角色接口类
@Service
public class SysUserRoleService extends BaseService {

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    //【用户-角色】设置查询条件
    private LambdaQueryWrapper<SysUserRole> getMenuQueryCondition(SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> lambdaQuery = Wrappers.lambdaQuery();
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysUserRole::getId);
        if (ObjectUtils.isNotNull(sysUserRole)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysUserRole.getId()), SysUserRole::getId, sysUserRole.getId());
            //【用户编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysUserRole.getUserId()), SysUserRole::getUserId, sysUserRole.getUserId());
            //【角色编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysUserRole.getRoleId()), SysUserRole::getRoleId, sysUserRole.getRoleId());
        }
        return lambdaQuery;
    }


    //【用户-角色】分页查询
    public IPage<SysUserRole> getPage(Page<SysUserRole> page, SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> lambdaQuery = getMenuQueryCondition(sysUserRole);
        return sysUserRoleDao.selectPage(page, lambdaQuery);
    }
    
    //【用户-角色】查询列表
    public List<SysUserRole> getList(SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> lambdaQuery = getMenuQueryCondition(sysUserRole);
        return sysUserRoleDao.selectList(lambdaQuery);
    }
    
    //【用户-角色】根据id查询
    public SysUserRole get(String id) {
        return sysUserRoleDao.selectById(id);
    }
    
    //【用户-角色】新增
    public int insert(SysUserRole sysUserRole) {
        if(StringUtils.isEmpty(sysUserRole.getId())){
            sysUserRole.setId(IdGen.primaryKey());
        }
        return sysUserRoleDao.insert(sysUserRole);
    }
    
    //【用户-角色】修改
    public int update(SysUserRole sysUserRole) {
        return sysUserRoleDao.updateById(sysUserRole);
    }
    
    //【用户-角色】删除
    public int delete(String id) {
        return sysUserRoleDao.deleteById(id);
    }

}