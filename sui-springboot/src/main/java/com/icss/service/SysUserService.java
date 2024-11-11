package com.icss.service;

import com.icss.dao.SysUserDao;
import com.icss.entity.SysUser;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.icss.sys.base.module.office.dao.SysOfficeDao;
import com.icss.sys.base.module.office.entity.SysOffice;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.base.module.userRole.entity.SysUserRole;
import com.icss.sys.base.module.userRole.service.SysUserRoleService;
import com.icss.sys.utils.admin.IdGen;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.gen.GenUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//【后台用户】接口类
@Service
public class SysUserService extends BaseService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysOfficeDao sysOfficeDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysMessageService sysMessageService;

    //【后台用户】设置查询条件
    private QueryWrapper<SysUser> getSysUserQueryCondition(SysUser sysUser) {
        QueryWrapper<SysUser> lambdaQuery = new QueryWrapper<>();
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getId()), "id", sysUser.getId());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getRoleCode()), "role_code", sysUser.getRoleCode());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getLoginName()), "login_name", sysUser.getLoginName());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getUserName()), "user_name", sysUser.getUserName());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getOfficeId()), "office_id", sysUser.getOfficeId());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getPhone()), "phone", sysUser.getPhone());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getUserNo()), "user_no", sysUser.getUserNo());
        lambdaQuery.eq(StringUtils.isNotNull(sysUser.getSex()), "sex", sysUser.getSex());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getIdCard()), "id_card", sysUser.getIdCard());
        lambdaQuery.eq(StringUtils.isNotEmpty(sysUser.getEmail()), "email", sysUser.getEmail());
        lambdaQuery.eq(StringUtils.isNotNull(sysUser.getDisabled()), "disabled", sysUser.getDisabled());
        lambdaQuery.eq(StringUtils.isNotNull(sysUser.getCreateBy()), "create_by", sysUser.getCreateBy());
        lambdaQuery.eq(StringUtils.isNotNull(sysUser.getDelFlag()), "del_flag", sysUser.getDelFlag());
        return lambdaQuery;
    }

    //【后台用户】分页查询
    public IPage<SysUser> getPage(Page<SysUser> page, SysUser sysUser) {
        QueryWrapper<SysUser> lambdaQuery = getSysUserQueryCondition(sysUser);
        IPage<SysUser> iPage = sysUserDao.findByPage(page, lambdaQuery);
        List<SysUser> records = iPage.getRecords();
        for (SysUser record : records) {
            String id = record.getId();
            List<SysRole> roleList = sysRoleService.findRolesListByUserId(id);
            record.setRoleList(roleList);
            SysOffice office = sysOfficeDao.selectById(sysUser.getOfficeId());
            sysUser.setOffice(office);
        }
        return iPage;
    }

    //【后台用户】查询列表
    public List<SysUser> getList(SysUser sysUser) {
        QueryWrapper<SysUser> lambdaQuery = getSysUserQueryCondition(sysUser);
        List<SysUser> list = sysUserDao.findList(lambdaQuery);
        for (SysUser record : list) {
            String id = record.getId();
            List<SysRole> roleList = sysRoleService.findRolesListByUserId(id);
            record.setRoleList(roleList);
            SysOffice office = sysOfficeDao.selectById(sysUser.getOfficeId());
            sysUser.setOffice(office);
        }
        return list;
    }

    //【后台用户】查询列表
    public List<SysUser> getUserList(SysUser sysUser) {
        QueryWrapper<SysUser> lambdaQuery = getSysUserQueryCondition(sysUser);
        List<SysUser> list = sysUserDao.findList(lambdaQuery);
        return list;
    }

    //【后台用户】根据id查询
    public SysUser get(String id) {
        return sysUserDao.selectById(id);
    }

    //【后台用户】根据对象查询
    public SysUser get(SysUser sysUser) {
        QueryWrapper<SysUser> lambdaQuery = getSysUserQueryCondition(sysUser);
        return sysUserDao.selectOne(lambdaQuery);
    }

    //【后台用户】新增
    public int insert(SysUser sysUser) {
        sysUser.preInsert();
        sysUser.setUserNo(GenUtils.getRuleNo("NO-",5));
        return sysUserDao.insert(sysUser);
    }

    //【后台用户】修改
    public int update(SysUser sysUser) {
        sysUser.preUpdate();
        return sysUserDao.updateById(sysUser);
    }

    //【后台用户】删除
    public int delete(String id) {
        //删除关联的用户-角色数据
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(id);
        List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
        for (SysUserRole userRole : list) {
            sysUserRoleService.delete(userRole.getId());
        }
        //删除关联的消息数据
        SysMessage sysMessage = new SysMessage();
        sysMessage.setUserId(id);
        List<SysMessage> sysMessageList = sysMessageService.getList(sysMessage);
        for (SysMessage message : sysMessageList) {
            sysMessageService.delete(message.getId());
        }
        return sysUserDao.deleteById(id);
    }

    //【后台用户】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            //删除关联的用户-角色数据
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(id);
            List<SysUserRole> list = sysUserRoleService.getList(sysUserRole);
            for (SysUserRole userRole : list) {
                sysUserRoleService.delete(userRole.getId());
            }
            //删除关联的消息数据
            SysMessage sysMessage = new SysMessage();
            sysMessage.setUserId(id);
            List<SysMessage> sysMessageList = sysMessageService.getList(sysMessage);
            for (SysMessage message : sysMessageList) {
                sysMessageService.delete(message.getId());
            }
        }
        return sysUserDao.deleteBatchIds(Arrays.asList(ids));
    }

    //删除用户角色
    public int deleteRolesByUserId(String userId) {
        SysUserRole params = new SysUserRole();
        params.setUserId(userId);
        List<SysUserRole> list = sysUserRoleService.getList(params);
        for (SysUserRole sysUserRole : list) {
            sysUserRoleService.delete(sysUserRole.getId());
        }
        return list.size();
    }

    //更新用户角色
    public void updateUserRoles(SysUser sysUser) {
        SysUserRole entity = new SysUserRole();
        entity.setUserId(sysUser.getId());
        this.deleteRolesByUserId(sysUser.getId());
        List<SysRole> roleList = sysUser.getRoleList();
        if (roleList != null && roleList.size() > 0) {
            for (SysRole role : roleList) {
                SysRole sysRole = sysRoleService.get(role);
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(IdGen.primaryKey());
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(sysRole.getId());
                sysUserRoleService.insert(sysUserRole);
            }
        }
    }

    //根据登录名查找
    public SysUser findByLoginName(String loginName) {
        SysUser params = new SysUser();
        params.setLoginName(loginName);
        return this.get(params);
    }
}