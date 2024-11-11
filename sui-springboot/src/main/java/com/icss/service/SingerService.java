package com.icss.service;

import com.icss.dao.SingerDao;
import com.icss.entity.Singer;
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

//歌手信息接口类
@Service
public class SingerService extends BaseService {

    @Autowired
    private SingerDao singerDao;

    //【歌手信息】设置查询条件
    private LambdaQueryWrapper<Singer> getSingerQueryCondition(Singer singer) {
        LambdaQueryWrapper<Singer> lambdaQuery = this.getBaseQueryCondition(singer);
        //根据创建时间排序
        lambdaQuery.orderByDesc(Singer::getCreateDate);
        if (ObjectUtils.isNotNull(singer)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(singer.getId()), Singer::getId, singer.getId());
            //【姓名】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(singer.getName()), Singer::getName, singer.getName());
            //【性别】范围查询
            lambdaQuery.in(ObjectUtils.isNotNull(singer.getSexRange()), Singer::getSex, singer.getSexRange());
            //【性别】精确查询
            lambdaQuery.eq(ObjectUtils.isNotNull(singer.getSex()), Singer::getSex, singer.getSex());
            //【地区】范围查询
            lambdaQuery.in(ObjectUtils.isNotNull(singer.getRegionAreaRange()), Singer::getRegionArea, singer.getRegionAreaRange());
            //【地区】精确查询
            lambdaQuery.eq(ObjectUtils.isNotNull(singer.getRegionArea()), Singer::getRegionArea, singer.getRegionArea());
        }
        return lambdaQuery;
    }

    //【歌手信息】分页查询
    public IPage<Singer> getPage(Page<Singer> page, Singer singer) {
        LambdaQueryWrapper<Singer> lambdaQuery = getSingerQueryCondition(singer);
        return singerDao.selectPage(page, lambdaQuery);
    }
    
    //【歌手信息】查询列表
    public List<Singer> getList(Singer singer) {
        LambdaQueryWrapper<Singer> lambdaQuery = getSingerQueryCondition(singer);
        return singerDao.selectList(lambdaQuery);
    }
    
    //【歌手信息】根据id查询
    public Singer get(String id) {
        return singerDao.selectById(id);
    }

    //【歌手信息】根据对象查询
    public Singer get(Singer singer) {
    LambdaQueryWrapper<Singer> lambdaQuery = getSingerQueryCondition(singer);
        return singerDao.selectOne(lambdaQuery);
    }
    
    //【歌手信息】新增
    public int insert(Singer singer) {
        this.preInsert(singer);
        return singerDao.insert(singer);
    }
    
    //【歌手信息】修改
    public int update(Singer singer) {
        this.preUpdate(singer);
        return singerDao.updateById(singer);
    }
    
    //【歌手信息】删除
    public int delete(String id) {
        return singerDao.deleteById(id);
    }

    //【歌手信息】批量删除
    public int delAll(String[] ids) {
        return singerDao.deleteBatchIds(Arrays.asList(ids));
    }

}