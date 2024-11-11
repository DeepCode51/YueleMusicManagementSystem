package com.icss.sys.base.module.log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.sys.base.module.log.entity.SysLog;
import org.springframework.stereotype.Repository;

/**
* 【日志信息】数据接口
* SysLogDao与SysLogDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysLogDao extends BaseMapper<SysLog> {

}

