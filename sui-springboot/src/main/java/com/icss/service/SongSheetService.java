package com.icss.service;

import com.icss.dao.SongSheetDao;
import com.icss.entity.SongSheet;
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

//歌单信息接口类
@Service
public class SongSheetService extends BaseService {

    @Autowired
    private SongSheetDao songSheetDao;

    //【歌单信息】设置查询条件
    private LambdaQueryWrapper<SongSheet> getSongSheetQueryCondition(SongSheet songSheet) {
        LambdaQueryWrapper<SongSheet> lambdaQuery = this.getBaseQueryCondition(songSheet);
        //根据创建时间排序
        lambdaQuery.orderByDesc(SongSheet::getCreateDate);
        if (ObjectUtils.isNotNull(songSheet)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(songSheet.getId()), SongSheet::getId, songSheet.getId());
            //【歌单标题】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(songSheet.getTitle()), SongSheet::getTitle, songSheet.getTitle());
            //【歌单分类】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(songSheet.getCategoryId()), SongSheet::getCategoryId, songSheet.getCategoryId());
        }
        return lambdaQuery;
    }

    //【歌单信息】分页查询
    public IPage<SongSheet> getPage(Page<SongSheet> page, SongSheet songSheet) {
        LambdaQueryWrapper<SongSheet> lambdaQuery = getSongSheetQueryCondition(songSheet);
        return songSheetDao.selectPage(page, lambdaQuery);
    }

    //【歌单信息】查询列表
    public List<SongSheet> getList(SongSheet songSheet) {
        LambdaQueryWrapper<SongSheet> lambdaQuery = getSongSheetQueryCondition(songSheet);
        return songSheetDao.selectList(lambdaQuery);
    }

    //【歌单信息】根据id查询
    public SongSheet get(String id) {
        return songSheetDao.selectById(id);
    }

    //【歌单信息】根据对象查询
    public SongSheet get(SongSheet songSheet) {
        LambdaQueryWrapper<SongSheet> lambdaQuery = getSongSheetQueryCondition(songSheet);
        return songSheetDao.selectOne(lambdaQuery);
    }

    //【歌单信息】新增
    public int insert(SongSheet songSheet) {
        this.preInsert(songSheet);
        return songSheetDao.insert(songSheet);
    }

    //【歌单信息】修改
    public int update(SongSheet songSheet) {
        this.preUpdate(songSheet);
        return songSheetDao.updateById(songSheet);
    }

    //【歌单信息】删除
    public int delete(String id) {
        return songSheetDao.deleteById(id);
    }

    //【歌单信息】批量删除
    public int delAll(String[] ids) {
        return songSheetDao.deleteBatchIds(Arrays.asList(ids));
    }

}