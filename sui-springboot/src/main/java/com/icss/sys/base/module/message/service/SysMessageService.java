package com.icss.sys.base.module.message.service;

import com.icss.entity.Register;
import com.icss.service.RegisterService;
import com.icss.entity.SysUser;
import com.icss.service.SysUserService;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.message.dao.SysMessageDao;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

//消息信息接口类
@Service
public class SysMessageService extends BaseService {

    @Autowired
    private SysMessageDao sysMessageDao;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private SysUserService sysUserService;

    //【消息信息】设置查询条件
    private LambdaQueryWrapper<SysMessage> getSysMessageQueryCondition(SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lambdaQuery = this.getBaseQueryCondition(sysMessage);
        lambdaQuery.orderByAsc(SysMessage::getId);
        if (ObjectUtils.isNotNull(sysMessage)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysMessage.getId()), SysMessage::getId, sysMessage.getId());
            //【消息类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysMessage.getType()), SysMessage::getType, sysMessage.getType());
            //【父级id】模糊查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysMessage.getPid()), SysMessage::getPid, sysMessage.getPid());
            //【用户id】模糊查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysMessage.getUserId()), SysMessage::getUserId, sysMessage.getUserId());
            //【关联id】模糊查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysMessage.getRefId()), SysMessage::getRefId, sysMessage.getRefId());
            //【星级】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysMessage.getStars()), SysMessage::getStars, sysMessage.getStars());
        }
        return lambdaQuery;
    }

    //【消息信息】分页查询
    public IPage<SysMessage> getPage(Page<SysMessage> page, SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lambdaQuery = getSysMessageQueryCondition(sysMessage);
        return sysMessageDao.selectPage(page, lambdaQuery);
    }
    //【消息信息】分页查询
    public IPage<SysMessage> getTreePage(Page<SysMessage> page, SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lambdaQuery = getSysMessageQueryCondition(sysMessage);
        Page<SysMessage> iPage = sysMessageDao.selectPage(page, lambdaQuery);
        List<SysMessage> records = iPage.getRecords();
        for (SysMessage message : records) {
            Register register = registerService.get(message.getUserId());
            setInfo(message, register);
            SysMessage params = new SysMessage();
            params.setPid(message.getId());
            List<SysMessage> childrens = this.getList(params);
            for (SysMessage children : childrens) {
                Register reg = registerService.get(children.getUserId());
                setInfo(children, reg);
            }
            message.setChildren(childrens);
        }
        return iPage;
    }

    //【消息信息】查询列表
    public List<SysMessage> getList(SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lambdaQuery = getSysMessageQueryCondition(sysMessage);
        return sysMessageDao.selectList(lambdaQuery);
    }
    //【消息信息】查询树列表
    public List<SysMessage> getTreeList(SysMessage sysMessage) {
        List<SysMessage> list = this.getList(sysMessage);
        for (SysMessage message : list) {
            Register register = registerService.get(message.getUserId());
            setInfo(message, register);
            SysMessage params = new SysMessage();
            params.setPid(message.getId());
            List<SysMessage> childrens = this.getList(params);
            for (SysMessage children : childrens) {
                Register reg = registerService.get(children.getUserId());
                setInfo(children, reg);
            }
            message.setChildren(childrens);
        }
        return list;
    }

    //填充用户信息
    private void setInfo(SysMessage message, Register register) {
        if (ObjectUtils.isNotNull(register)) {
            message.setPhoto(register.getPhoto());
            message.setLoginName(register.getLoginName());
            message.setUserName(register.getUserName());
            message.setEmail(register.getEmail());
        } else {
            SysUser sysUser = sysUserService.get(message.getUserId());
            if (ObjectUtils.isNotNull(sysUser)) {
                message.setPhoto(sysUser.getPhoto());
                message.setLoginName(sysUser.getLoginName());
                message.setUserName(sysUser.getUserName());
                message.setEmail(sysUser.getEmail());
            }
        }
    }

    //【消息信息】根据id查询
    public SysMessage get(String id) {
        return sysMessageDao.selectById(id);
    }
    //【消息信息】根据对象查询
    public SysMessage get(SysMessage sysMessage) {
        LambdaQueryWrapper<SysMessage> lambdaQuery = getSysMessageQueryCondition(sysMessage);
        return sysMessageDao.selectOne(lambdaQuery);
    }

    //【消息信息】新增
    public int insert(SysMessage sysMessage) {
        this.preInsert(sysMessage);
        return sysMessageDao.insert(sysMessage);
    }

    //【消息信息】修改
    public int update(SysMessage sysMessage) {
        this.preUpdate(sysMessage);
        return sysMessageDao.updateById(sysMessage);
    }

    //【消息信息】删除
    public int delete(String id) {
        return sysMessageDao.deleteById(id);
    }

    //【消息信息】批量删除
    public int delAll(String[] ids) {
        return sysMessageDao.deleteBatchIds(Arrays.asList(ids));
    }

}