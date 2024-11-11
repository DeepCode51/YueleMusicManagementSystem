package com.icss.sys.base.module.column.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.column.dao.SysColumnDao;
import com.icss.sys.base.module.column.entity.SysColumn;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//字段信息接口类
@Service
public class SysColumnService extends BaseService {

    @Autowired
    private SysColumnDao sysColumnDao;

    //【字段信息】设置查询条件
    private LambdaQueryWrapper<SysColumn> getSysColumnQueryCondition(SysColumn sysColumn) {
        LambdaQueryWrapper<SysColumn> lambdaQuery = this.getBaseQueryCondition(sysColumn);
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysColumn::getTableName);
        if (ObjectUtils.isNotNull(sysColumn)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getId()), SysColumn::getId, sysColumn.getId());
            //【表名称】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getTableName()), SysColumn::getTableName, sysColumn.getTableName());
            //【表描述】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getTableDesc()), SysColumn::getTableDesc, sysColumn.getTableDesc());
            //【列名称】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getColumnName()), SysColumn::getColumnName, sysColumn.getColumnName());
            //【列描述】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getColumnDesc()), SysColumn::getColumnDesc, sysColumn.getColumnDesc());
            //【控件类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getInputType()), SysColumn::getInputType, sysColumn.getInputType());
            //【查询类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getQueryType()), SysColumn::getQueryType, sysColumn.getQueryType());
            //【字典类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getDictType()), SysColumn::getDictType, sysColumn.getDictType());
            //【关联类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysColumn.getAssociateType()), SysColumn::getAssociateType, sysColumn.getAssociateType());
            //【是否隐藏】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysColumn.getIsHidden()), SysColumn::getIsHidden, sysColumn.getIsHidden());
            //【是否唯一】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysColumn.getIsUnique()), SysColumn::getIsUnique, sysColumn.getIsUnique());
            //【是否主键】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysColumn.getIsPk()), SysColumn::getIsPk, sysColumn.getIsPk());
            //【是否显示】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysColumn.getIsShow()), SysColumn::getIsShow, sysColumn.getIsShow());
            //【排序】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysColumn.getSort()), SysColumn::getSort, sysColumn.getSort());
        }
        return lambdaQuery;
    }

    //【字段信息】分页查询
    public IPage<SysColumn> getPage(Page<SysColumn> page, SysColumn sysColumn) {
        LambdaQueryWrapper<SysColumn> lambdaQuery = getSysColumnQueryCondition(sysColumn);
        return sysColumnDao.selectPage(page, lambdaQuery);
    }
    
    //【字段信息】查询列表
    public List<SysColumn> getList(SysColumn sysColumn) {
        LambdaQueryWrapper<SysColumn> lambdaQuery = getSysColumnQueryCondition(sysColumn);
        return sysColumnDao.selectList(lambdaQuery);
    }
    
    //【字段信息】根据id查询
    public SysColumn get(String id) {
        return sysColumnDao.selectById(id);
    }

    //【字段信息】根据对象查询
    public SysColumn get(SysColumn sysColumn) {
    LambdaQueryWrapper<SysColumn> lambdaQuery = getSysColumnQueryCondition(sysColumn);
        return sysColumnDao.selectOne(lambdaQuery);
    }
    
    //【字段信息】新增
    public int insert(SysColumn sysColumn) {
        this.preInsert(sysColumn);
        return sysColumnDao.insert(sysColumn);
    }
    
    //【字段信息】修改
    public int update(SysColumn sysColumn) {
        this.preUpdate(sysColumn);
        return sysColumnDao.updateById(sysColumn);
    }
    
    //【字段信息】删除
    public int delete(String id) {
        return sysColumnDao.deleteById(id);
    }

    //【字段信息】批量删除
    public int delAll(String[] ids) {
        return sysColumnDao.deleteBatchIds(Arrays.asList(ids));
    }

}