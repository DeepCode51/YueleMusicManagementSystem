package com.icss.dao;

import com.icss.entity.SongSheet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* 【歌单信息】数据接口
* SongSheetDao与SongSheetDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SongSheetDao extends BaseMapper<SongSheet> {

}

