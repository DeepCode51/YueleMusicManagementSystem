package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.PlayList;
import org.springframework.stereotype.Repository;

/**
* 【播放清单信息】数据接口
* PlayListDao与PlayListDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface PlayListDao extends BaseMapper<PlayList> {

}

