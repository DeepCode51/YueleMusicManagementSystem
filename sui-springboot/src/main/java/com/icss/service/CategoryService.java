package com.icss.service;

import com.icss.dao.CategoryDao;
import com.icss.entity.Category;
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

//类别信息接口类
@Service
public class CategoryService extends BaseService {

    @Autowired
    private CategoryDao categoryDao;

    //【类别信息】设置查询条件
    private LambdaQueryWrapper<Category> getCategoryQueryCondition(Category category) {
        LambdaQueryWrapper<Category> lambdaQuery = this.getBaseQueryCondition(category);
        //根据创建时间排序
        lambdaQuery.orderByDesc(Category::getCreateDate);
        if (ObjectUtils.isNotNull(category)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(category.getId()), Category::getId, category.getId());
            //【类别名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(category.getName()), Category::getName, category.getName());
        }
        return lambdaQuery;
    }

    //【类别信息】分页查询
    public IPage<Category> getPage(Page<Category> page, Category category) {
        LambdaQueryWrapper<Category> lambdaQuery = getCategoryQueryCondition(category);
        return categoryDao.selectPage(page, lambdaQuery);
    }
    
    //【类别信息】查询列表
    public List<Category> getList(Category category) {
        LambdaQueryWrapper<Category> lambdaQuery = getCategoryQueryCondition(category);
        return categoryDao.selectList(lambdaQuery);
    }
    
    //【类别信息】根据id查询
    public Category get(String id) {
        return categoryDao.selectById(id);
    }

    //【类别信息】根据对象查询
    public Category get(Category category) {
    LambdaQueryWrapper<Category> lambdaQuery = getCategoryQueryCondition(category);
        return categoryDao.selectOne(lambdaQuery);
    }
    
    //【类别信息】新增
    public int insert(Category category) {
        this.preInsert(category);
        return categoryDao.insert(category);
    }
    
    //【类别信息】修改
    public int update(Category category) {
        this.preUpdate(category);
        return categoryDao.updateById(category);
    }
    
    //【类别信息】删除
    public int delete(String id) {
        return categoryDao.deleteById(id);
    }

    //【类别信息】批量删除
    public int delAll(String[] ids) {
        return categoryDao.deleteBatchIds(Arrays.asList(ids));
    }

}