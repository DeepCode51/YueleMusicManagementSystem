package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.Singer;
import org.springframework.stereotype.Repository;

/**
* 【歌手信息】数据接口
* SingerDao与SingerDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SingerDao extends BaseMapper<Singer> {

}

