package com.icss.sys.base.module.office.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.sys.base.module.office.entity.SysOffice;
import org.springframework.stereotype.Repository;

/**
* 【机构信息】数据接口
* SysOfficeDao与SysOfficeDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysOfficeDao extends BaseMapper<SysOffice> {
}

