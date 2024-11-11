package com.icss.sys.base.module.config.dao;

import com.icss.sys.base.module.config.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* 【系统配置信息】数据接口
* SysConfigDao与SysConfigDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysConfigDao extends BaseMapper<SysConfig> {

}

