package com.icss.service;

import com.icss.dao.ListSongDao;
import com.icss.entity.ListSong;
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

//歌单关联信息接口类
@Service
public class ListSongService extends BaseService {

    @Autowired
    private ListSongDao listSongDao;

    //【歌单关联信息】设置查询条件
    private LambdaQueryWrapper<ListSong> getListSongQueryCondition(ListSong listSong) {
        LambdaQueryWrapper<ListSong> lambdaQuery = this.getBaseQueryCondition(listSong);
        //根据创建时间排序
        lambdaQuery.orderByDesc(ListSong::getCreateDate);
        if (ObjectUtils.isNotNull(listSong)) {
            //【主键】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(listSong.getId()), ListSong::getId, listSong.getId());
            //【所属歌单】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(listSong.getSongSheetId()), ListSong::getSongSheetId, listSong.getSongSheetId());
            //【所属歌曲】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(listSong.getSongId()), ListSong::getSongId, listSong.getSongId());
        }
        return lambdaQuery;
    }

    //【歌单关联信息】分页查询
    public IPage<ListSong> getPage(Page<ListSong> page, ListSong listSong) {
        LambdaQueryWrapper<ListSong> lambdaQuery = getListSongQueryCondition(listSong);
        return listSongDao.selectPage(page, lambdaQuery);
    }
    
    //【歌单关联信息】查询列表
    public List<ListSong> getList(ListSong listSong) {
        LambdaQueryWrapper<ListSong> lambdaQuery = getListSongQueryCondition(listSong);
        return listSongDao.selectList(lambdaQuery);
    }
    
    //【歌单关联信息】根据id查询
    public ListSong get(String id) {
        return listSongDao.selectById(id);
    }

    //【歌单关联信息】根据对象查询
    public ListSong get(ListSong listSong) {
    LambdaQueryWrapper<ListSong> lambdaQuery = getListSongQueryCondition(listSong);
        return listSongDao.selectOne(lambdaQuery);
    }
    
    //【歌单关联信息】新增
    public int insert(ListSong listSong) {
        this.preInsert(listSong);
        return listSongDao.insert(listSong);
    }
    
    //【歌单关联信息】修改
    public int update(ListSong listSong) {
        this.preUpdate(listSong);
        return listSongDao.updateById(listSong);
    }
    
    //【歌单关联信息】删除
    public int delete(String id) {
        return listSongDao.deleteById(id);
    }

    //【歌单关联信息】批量删除
    public int delAll(String[] ids) {
        return listSongDao.deleteBatchIds(Arrays.asList(ids));
    }

}