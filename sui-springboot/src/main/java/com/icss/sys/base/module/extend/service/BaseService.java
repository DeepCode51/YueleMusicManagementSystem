package com.icss.sys.base.module.extend.service;

import com.icss.sys.base.module.attach.entity.SysAttach;
import com.icss.sys.base.module.attach.service.SysAttachService;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.icss.sys.utils.admin.IdGen;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//基础接口类
@Service
public abstract class BaseService<D extends BaseMapper<T>, T extends BaseEntity> {
    @Autowired
    private SysAttachService sysAttachService;

    //设置查询条件
    protected LambdaQueryWrapper<T> getBaseQueryCondition(T t) {
        LambdaQueryWrapper<T> lambdaQuery = Wrappers.lambdaQuery();
        if (ObjectUtils.isNotNull(t)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(t.getId()), T::getId, t.getId());
            //【创建人】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(t.getCreateBy()), T::getCreateBy, t.getCreateBy());
            //【创建时间】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(t.getCreateDate()), T::getCreateDate, t.getCreateDate());
            //【更新人】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(t.getUpdateBy()), T::getUpdateBy, t.getUpdateBy());
            //【更新时间】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(t.getUpdateDate()), T::getUpdateDate, t.getUpdateDate());
            //【删除标识】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(t.getDelFlag()), T::getDelFlag, t.getDelFlag());
        }
        return lambdaQuery;
    }

    //对附件信息进行处理
    protected boolean preInsert(T entity) {
        entity.preInsert();
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(IdGen.primaryKey());//外层未设置id则由这里设置
        }
        List<String> fileList = entity.getFileList();
        if (fileList!=null&&fileList.size() > 0) {
            for (String fileId : fileList) {
                SysAttach sysAttach = sysAttachService.get(fileId);
                if (ObjectUtils.isNotNull(sysAttach)) {
                    sysAttach.setRefId(entity.getId());
                    sysAttachService.update(sysAttach);
                }
            }
        }
        return true;
    }
    //对附件信息进行处理
    protected boolean preUpdate(T entity) {
        entity.preUpdate();
        List<String> fileList = entity.getFileList();
        if (fileList!=null&&fileList.size() > 0) {
            for (String fileId : fileList) {
                SysAttach sysAttach = sysAttachService.get(fileId);
                if (ObjectUtils.isNotNull(sysAttach)) {
                    sysAttach.setRefId(entity.getId());
                    sysAttachService.update(sysAttach);
                }
            }
        }
        return true;
    }
}
