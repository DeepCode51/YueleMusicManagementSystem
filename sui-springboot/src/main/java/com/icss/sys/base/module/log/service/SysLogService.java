package com.icss.sys.base.module.log.service;

import com.icss.entity.SysUser;
import com.icss.sys.base.enums.SystemType;
import com.icss.sys.utils.admin.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.log.dao.SysLogDao;
import com.icss.sys.base.module.log.entity.SysLog;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.icss.sys.base.aop.SystemLogAspect.getIpAddress;

//日志信息接口类
@Service
public class SysLogService extends BaseService {

    @Autowired
    private SysLogDao sysLogDao;

    //【日志信息】设置查询条件
    private LambdaQueryWrapper<SysLog> getSysLogQueryCondition(SysLog sysLog) {
        LambdaQueryWrapper<SysLog> lambdaQuery = this.getBaseQueryCondition(sysLog);
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysLog::getCreateDate);
        if (ObjectUtils.isNotNull(sysLog)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysLog.getId()), SysLog::getId, sysLog.getId());
            //【用户名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getLoginName()), SysLog::getLoginName, sysLog.getLoginName());
            //【用户ID】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getUserId()), SysLog::getUserId, sysLog.getUserId());
            //【系统类型】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysLog.getSystemType()), SysLog::getSystemType, sysLog.getSystemType());
            //【模块名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getModuleName()), SysLog::getModuleName, sysLog.getModuleName());
            //【方法类型】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getMethod()), SysLog::getMethod, sysLog.getMethod());
            //【操作日期】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysLog.getOperationDate()), SysLog::getOperationDate, sysLog.getOperationDate());
            //【请求URI】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getRequestUri()), SysLog::getRequestUri, sysLog.getRequestUri());
            //【请求端口】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getRemotePort()), SysLog::getRemotePort, sysLog.getRemotePort());
            //【本地主机】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getLocalName()), SysLog::getLocalName, sysLog.getLocalName());
            //【本地地址】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getLocalAddr()), SysLog::getLocalAddr, sysLog.getLocalAddr());
            //【远程主机】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getRemoteHost()), SysLog::getRemoteHost, sysLog.getRemoteHost());
            //【远程地址】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysLog.getRemoteAddr()), SysLog::getRemoteAddr, sysLog.getRemoteAddr());
        }
        return lambdaQuery;
    }

    //【日志信息】分页查询
    public IPage<SysLog> getPage(Page<SysLog> page, SysLog sysLog) {
        LambdaQueryWrapper<SysLog> lambdaQuery = getSysLogQueryCondition(sysLog);
        return sysLogDao.selectPage(page, lambdaQuery);
    }
    
    //【日志信息】查询列表
    public List<SysLog> getList(SysLog sysLog) {
        LambdaQueryWrapper<SysLog> lambdaQuery = getSysLogQueryCondition(sysLog);
        return sysLogDao.selectList(lambdaQuery);
    }
    
    //【日志信息】根据id查询
    public SysLog get(String id) {
        return sysLogDao.selectById(id);
    }
    
    //【日志信息】新增
    public int insert(SysLog sysLog) {
        this.preInsert(sysLog);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setRemoteAddr(getIpAddress(request));
        sysLog.setRemoteHost(request.getRemoteHost());
        sysLog.setLocalAddr(request.getLocalAddr());
        sysLog.setLocalName(request.getLocalName());
        sysLog.setMethod(request.getMethod());
        sysLog.setRequestUri(request.getRequestURI());
        sysLog.setRemotePort(String.valueOf(request.getRemotePort()));
        sysLog.setOperationDate(new Date());
        SysUser userInfo = ShiroUtils.getUserInfo();
        sysLog.setLoginName(userInfo==null?"":userInfo.getLoginName());
        sysLog.setUserId(ShiroUtils.getCurrentUserId());
        sysLog.setSystemType(Integer.valueOf(SystemType.ADMIN.getValue()));
        return sysLogDao.insert(sysLog);
    }
    
    //【日志信息】修改
    public int update(SysLog sysLog) {
        this.preUpdate(sysLog);
        return sysLogDao.updateById(sysLog);
    }
    
    //【日志信息】删除
    public int delete(String id) {
        return sysLogDao.deleteById(id);
    }

    //【日志信息】批量删除
    public int delAll(String[] ids) {
        return sysLogDao.deleteBatchIds(Arrays.asList(ids));
    }

}