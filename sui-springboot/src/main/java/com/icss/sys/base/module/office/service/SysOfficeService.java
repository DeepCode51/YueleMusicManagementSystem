package com.icss.sys.base.module.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.module.office.dao.SysOfficeDao;
import com.icss.sys.base.module.office.entity.SysOffice;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//机构信息接口类
@Service
public class SysOfficeService extends BaseService {

    @Autowired
    private SysOfficeDao sysOfficeDao;

    //【机构信息】设置查询条件
    private LambdaQueryWrapper<SysOffice> getSysOfficeQueryCondition(SysOffice sysOffice) {
        LambdaQueryWrapper<SysOffice> lambdaQuery = this.getBaseQueryCondition(sysOffice);
        //根据创建时间排序
        lambdaQuery.orderByAsc(SysOffice::getUpdateDate);
        if (ObjectUtils.isNotNull(sysOffice)) {
            //【父级编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysOffice.getPid()), SysOffice::getPid, sysOffice.getPid());
            //【名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysOffice.getName()), SysOffice::getName, sysOffice.getName());
            //【编码】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysOffice.getCode()), SysOffice::getCode, sysOffice.getCode());
            //【类型】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysOffice.getType()), SysOffice::getType, sysOffice.getType());
            //【排序】精确查询
            lambdaQuery.eq(StringUtils.isNotNull(sysOffice.getSort()), SysOffice::getSort, sysOffice.getSort());
        }
        return lambdaQuery;
    }

    //【机构信息】分页查询
    public IPage<SysOffice> getPage(Page<SysOffice> page, SysOffice sysOffice) {
        LambdaQueryWrapper<SysOffice> lambdaQuery = getSysOfficeQueryCondition(sysOffice);
        return sysOfficeDao.selectPage(page, lambdaQuery);
    }
    
    //【机构信息】查询列表
    public List<SysOffice> getList(SysOffice sysOffice) {
        LambdaQueryWrapper<SysOffice> lambdaQuery = getSysOfficeQueryCondition(sysOffice);
        return sysOfficeDao.selectList(lambdaQuery);
    }
    
    //【机构信息】根据id查询
    public SysOffice get(String id) {
        return sysOfficeDao.selectById(id);
    }

    //【机构信息】根据对象查询
    public SysOffice get(SysOffice sysOffice) {
        LambdaQueryWrapper<SysOffice> lambdaQuery = getSysOfficeQueryCondition(sysOffice);
        return sysOfficeDao.selectOne(lambdaQuery);
    }
    
    //【机构信息】新增
    public int insert(SysOffice sysOffice) {
        this.preInsert(sysOffice);
        return sysOfficeDao.insert(sysOffice);
    }
    
    //【机构信息】修改
    public int update(SysOffice sysOffice) {
        this.preUpdate(sysOffice);
        return sysOfficeDao.updateById(sysOffice);
    }
    
    //【机构信息】删除
    public int delete(String id) {
        //删除子级所有机构
        SysOffice office = new SysOffice();
        office.setPid(id);
        List<SysOffice> list = this.getList(office);
        for (SysOffice sysOffice : list) {
            this.delete(sysOffice.getId());
        }
        return sysOfficeDao.deleteById(id);
    }

    //【机构信息】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            //删除子级所有机构
            SysOffice office = new SysOffice();
            office.setPid(id);
            List<SysOffice> list = this.getList(office);
            for (SysOffice sysOffice : list) {
                this.delete(sysOffice.getId());
            }
        }
        return sysOfficeDao.deleteBatchIds(Arrays.asList(ids));
    }

    //机构树节点递归
    public List<SysOffice> getOfficeChildren(List<SysOffice> allList) {
        allList.forEach(item -> {
            SysOffice entity = new SysOffice();
            entity.setPid(item.getId());
            List<SysOffice> children = this.getList(entity);
            if(children.size()>0){
                item.setChildren(children);
            }
            if (item.getChildren() != null && item.getChildren().size() > 0) {//递归
                getOfficeChildren(item.getChildren());
            }
        });
        return allList;
    }

}