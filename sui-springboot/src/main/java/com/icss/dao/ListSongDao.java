package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.ListSong;
import org.springframework.stereotype.Repository;

/**
* 【歌单关联信息】数据接口
* ListSongDao与ListSongDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface ListSongDao extends BaseMapper<ListSong> {

}

