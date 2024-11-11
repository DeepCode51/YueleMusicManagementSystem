package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.Notice;
import org.springframework.stereotype.Repository;

/**
* 【通知信息】数据接口
* NoticeDao与NoticeDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface NoticeDao extends BaseMapper<Notice> {

}

