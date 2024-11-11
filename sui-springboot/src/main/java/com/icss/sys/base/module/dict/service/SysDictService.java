package com.icss.sys.base.module.dict.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.base.module.dict.dao.SysDictDao;
import com.icss.sys.base.module.dict.entity.DictTree;
import com.icss.sys.base.module.dict.entity.SysDict;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.base.module.extend.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//字典信息接口类
@Service
public class SysDictService extends BaseService {

    @Autowired
    private SysDictDao sysDictDao;


    //【字典信息】设置查询条件
    private LambdaQueryWrapper<SysDict> getSysDictQueryCondition(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> lambdaQuery = this.getBaseQueryCondition(sysDict);
        //根据创建时间排序
        lambdaQuery.orderByAsc(SysDict::getSort);
        if (ObjectUtils.isNotNull(sysDict)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysDict.getId()), SysDict::getId, sysDict.getId());
            //【父级编号】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysDict.getPid()), SysDict::getPid, sysDict.getPid());
            //【标签名】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysDict.getLabel()), SysDict::getLabel, sysDict.getLabel());
            //【数据值】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysDict.getValue()), SysDict::getValue, sysDict.getValue());
            //【类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysDict.getType()), SysDict::getType, sysDict.getType());
            //【样式】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysDict.getStyle()), SysDict::getStyle, sysDict.getStyle());
            //【排序】精确查询
            //lambdaQuery.eq(StringUtils.isNotNull(sysDict.getSort()),SysDict::getSort, sysDict.getSort());
            //【备注信息】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysDict.getRemarks()), SysDict::getRemarks, sysDict.getRemarks());
        }
        return lambdaQuery;
    }

    //【字典信息】分页查询
    public IPage<SysDict> getPage(Page<SysDict> page, SysDict sysDict) {
        LambdaQueryWrapper<SysDict> lambdaQuery = getSysDictQueryCondition(sysDict);
        return sysDictDao.selectPage(page, lambdaQuery);
    }

    //【字典信息】查询列表
    public List<SysDict> getList(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> lambdaQuery = getSysDictQueryCondition(sysDict);
        return sysDictDao.selectList(lambdaQuery);
    }

    //【字典信息】根据id查询
    public SysDict get(String id) {
        return sysDictDao.selectById(id);
    }

    //【字典信息】新增
    public int insert(SysDict sysDict) {
        this.preInsert(sysDict);
        return sysDictDao.insert(sysDict);
    }

    //【字典信息】修改
    public int update(SysDict sysDict) {
        this.preUpdate(sysDict);
        return sysDictDao.updateById(sysDict);
    }


    //【字典信息】删除
    public int delete(String id) {
        SysDict sysDict = new SysDict();
        sysDict.setPid(id);
        List<SysDict> list = this.getList(sysDict);
        for (SysDict dict : list) {
            this.delete(dict.getId());
        }
        return sysDictDao.deleteById(id);
    }


    //【字典信息】批量删除
    public int delAll(String[] ids) {
        for (String id : ids) {
            SysDict sysDict = new SysDict();
            sysDict.setPid(id);
            List<SysDict> list = this.getList(sysDict);
            for (SysDict dict : list) {
                this.delete(dict.getId());
            }
        }
        return sysDictDao.deleteBatchIds(Arrays.asList(ids));
    }

    public List<DictTree> getDictTree(SysDict sysDict) {
        return sysDictDao.getDictTree(sysDict);
    }

    //字典树节点递归
    public List<SysDict> getMenuChildren(List<SysDict> allList) {
        allList.forEach(item -> {
            SysDict entity = new SysDict();
            entity.setPid(item.getId());
            List<SysDict> children = this.getList(entity);
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