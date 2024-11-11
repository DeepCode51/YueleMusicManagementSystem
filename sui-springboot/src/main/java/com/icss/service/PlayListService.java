package com.icss.service;

import com.icss.dao.PlayListDao;
import com.icss.entity.PlayList;
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

//播放清单信息接口类
@Service
public class PlayListService extends BaseService {

    @Autowired
    private PlayListDao playListDao;

    //【播放清单信息】设置查询条件
    private LambdaQueryWrapper<PlayList> getPlayListQueryCondition(PlayList playList) {
        LambdaQueryWrapper<PlayList> lambdaQuery = this.getBaseQueryCondition(playList);
        //根据创建时间排序
        lambdaQuery.orderByDesc(PlayList::getId);
        if (ObjectUtils.isNotNull(playList)) {
            //【所属用户】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(playList.getUserId()), PlayList::getUserId, playList.getUserId());
            //【所属音乐】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(playList.getSongId()), PlayList::getSongId, playList.getSongId());
        }
        return lambdaQuery;
    }

    //【播放清单信息】分页查询
    public IPage<PlayList> getPage(Page<PlayList> page, PlayList playList) {
        LambdaQueryWrapper<PlayList> lambdaQuery = getPlayListQueryCondition(playList);
        return playListDao.selectPage(page, lambdaQuery);
    }
    
    //【播放清单信息】查询列表
    public List<PlayList> getList(PlayList playList) {
        LambdaQueryWrapper<PlayList> lambdaQuery = getPlayListQueryCondition(playList);
        return playListDao.selectList(lambdaQuery);
    }
    
    //【播放清单信息】根据id查询
    public PlayList get(String id) {
        return playListDao.selectById(id);
    }

    //【播放清单信息】根据对象查询
    public PlayList get(PlayList playList) {
    LambdaQueryWrapper<PlayList> lambdaQuery = getPlayListQueryCondition(playList);
        return playListDao.selectOne(lambdaQuery);
    }
    
    //【播放清单信息】新增
    public int insert(PlayList playList) {
        this.preInsert(playList);
        return playListDao.insert(playList);
    }
    
    //【播放清单信息】修改
    public int update(PlayList playList) {
        this.preUpdate(playList);
        return playListDao.updateById(playList);
    }
    
    //【播放清单信息】删除
    public int delete(String id) {
        return playListDao.deleteById(id);
    }

    //【播放清单信息】批量删除
    public int delAll(String[] ids) {
        return playListDao.deleteBatchIds(Arrays.asList(ids));
    }

}