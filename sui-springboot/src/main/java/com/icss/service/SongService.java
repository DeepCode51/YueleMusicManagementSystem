package com.icss.service;

import com.icss.dao.SongDao;
import com.icss.entity.Song;
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

//歌曲信息接口类
@Service
public class SongService extends BaseService {

    @Autowired
    private SongDao songDao;

    //【歌曲信息】设置查询条件
    private LambdaQueryWrapper<Song> getSongQueryCondition(Song song) {
        LambdaQueryWrapper<Song> lambdaQuery = this.getBaseQueryCondition(song);
        //根据创建时间排序
        lambdaQuery.orderByDesc(Song::getCreateDate);
        if (ObjectUtils.isNotNull(song)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(song.getId()), Song::getId, song.getId());
            //【歌曲名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(song.getName()), Song::getName, song.getName());
            //【歌手姓名】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(song.getSingerId()), Song::getSingerId, song.getSingerId());
            //【歌曲地址】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(song.getUrl()), Song::getUrl, song.getUrl());
        }
        return lambdaQuery;
    }

    //【歌曲信息】分页查询
    public IPage<Song> getPage(Page<Song> page, Song song) {
        LambdaQueryWrapper<Song> lambdaQuery = getSongQueryCondition(song);
        return songDao.selectPage(page, lambdaQuery);
    }
    
    //【歌曲信息】查询列表
    public List<Song> getList(Song song) {
        LambdaQueryWrapper<Song> lambdaQuery = getSongQueryCondition(song);
        return songDao.selectList(lambdaQuery);
    }
    
    //【歌曲信息】根据id查询
    public Song get(String id) {
        return songDao.selectById(id);
    }

    //【歌曲信息】根据对象查询
    public Song get(Song song) {
    LambdaQueryWrapper<Song> lambdaQuery = getSongQueryCondition(song);
        return songDao.selectOne(lambdaQuery);
    }
    
    //【歌曲信息】新增
    public int insert(Song song) {
        this.preInsert(song);
        return songDao.insert(song);
    }
    
    //【歌曲信息】修改
    public int update(Song song) {
        this.preUpdate(song);
        return songDao.updateById(song);
    }
    
    //【歌曲信息】删除
    public int delete(String id) {
        return songDao.deleteById(id);
    }

    //【歌曲信息】批量删除
    public int delAll(String[] ids) {
        return songDao.deleteBatchIds(Arrays.asList(ids));
    }

}