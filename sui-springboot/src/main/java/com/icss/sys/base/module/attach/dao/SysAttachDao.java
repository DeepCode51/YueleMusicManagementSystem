package com.icss.sys.base.module.attach.dao;

import com.icss.sys.base.module.attach.entity.SysAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* 【附件信息】数据接口
* SysAttachDao与SysAttachDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysAttachDao extends BaseMapper<SysAttach> {

    //int deleteByRefId(String refId);
}

