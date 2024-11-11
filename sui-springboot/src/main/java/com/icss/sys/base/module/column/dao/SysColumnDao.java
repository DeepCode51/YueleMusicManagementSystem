package com.icss.sys.base.module.column.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.sys.base.module.column.entity.SysColumn;
import org.springframework.stereotype.Repository;

/**
* 【字段信息】数据接口
* SysColumnDao与SysColumnDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysColumnDao extends BaseMapper<SysColumn> {

}

