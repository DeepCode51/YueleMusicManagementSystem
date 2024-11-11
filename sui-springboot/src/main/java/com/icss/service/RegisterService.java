package com.icss.service;

import com.icss.dao.RegisterDao;
import com.icss.entity.Register;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.base.module.extend.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//注册信息接口类
@Service
public class RegisterService extends BaseService {

    @Autowired
    private RegisterDao registerDao;
    @Autowired
    private SysMessageService sysMessageService;

    //【前台用户】设置查询条件
    private LambdaQueryWrapper<Register> getUserRegisterQueryCondition(Register register) {
        LambdaQueryWrapper<Register> lambdaQuery = this.getBaseQueryCondition(register);
        //根据创建时间排序
        lambdaQuery.orderByDesc(Register::getCreateDate);
        if (ObjectUtils.isNotNull(register)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getId()), Register::getId, register.getId());
            //【头像】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(register.getPhoto()), Register::getPhoto, register.getPhoto());
            //【登录名】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getLoginName()), Register::getLoginName, register.getLoginName());
            //【密码】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getPassword()), Register::getPassword, register.getPassword());
            //【姓名】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getUserName()), Register::getUserName, register.getUserName());
            //【性别】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(register.getSex()), Register::getSex, register.getSex());
            //【工号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getUserNo()), Register::getUserNo, register.getUserNo());
            //【手机号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getPhone()), Register::getPhone, register.getPhone());
            //【身份证号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getIdCard()), Register::getIdCard, register.getIdCard());
            //【邮箱】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(register.getEmail()), Register::getEmail, register.getEmail());
            //【用户类型】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(register.getUserType()), Register::getUserType, register.getUserType());
        }
        return lambdaQuery;
    }

    //【前台用户】分页查询
    public IPage<Register> getPage(Page<Register> page, Register register) {
        LambdaQueryWrapper<Register> lambdaQuery = getUserRegisterQueryCondition(register);
        return registerDao.selectPage(page, lambdaQuery);
    }

    //【前台用户】查询列表
    public List<Register> getList(Register register) {
        LambdaQueryWrapper<Register> lambdaQuery = getUserRegisterQueryCondition(register);
        return registerDao.selectList(lambdaQuery);
    }

    //【前台用户】根据id查询
    public Register get(String id) {
        return registerDao.selectById(id);
    }

    //【前台用户】根据对象查询
    public Register get(Register register) {
        LambdaQueryWrapper<Register> lambdaQuery = getUserRegisterQueryCondition(register);
        return registerDao.selectOne(lambdaQuery);
    }

    //【前台用户】新增
    public int insert(Register register) {
        this.preInsert(register);
        return registerDao.insert(register);
    }

    //【前台用户】修改
    public int update(Register register) {
        this.preUpdate(register);
        return registerDao.updateById(register);
    }

    //【前台用户】删除
    public int delete(String id) {
        Register register = registerDao.selectById(id);
        //删除关联的消息数据
        SysMessage params = new SysMessage();
        params.setUserId(id);
        List<SysMessage> list = sysMessageService.getList(params);
        for (SysMessage sysMessage : list) {
            sysMessageService.delete(sysMessage.getId());
        }
        return registerDao.deleteById(id);
    }

    //【前台用户】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            Register register = registerDao.selectById(id);
            //删除关联的消息数据
            SysMessage params = new SysMessage();
            params.setUserId(id);
            List<SysMessage> list = sysMessageService.getList(params);
            for (SysMessage sysMessage : list) {
                sysMessageService.delete(sysMessage.getId());
            }
        }
        return registerDao.deleteBatchIds(Arrays.asList(ids));
    }

}