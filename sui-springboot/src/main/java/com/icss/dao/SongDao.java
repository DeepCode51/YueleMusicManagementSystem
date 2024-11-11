package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.Song;
import org.springframework.stereotype.Repository;

/**
* 【歌曲信息】数据接口
* SongDao与SongDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SongDao extends BaseMapper<Song> {

}

