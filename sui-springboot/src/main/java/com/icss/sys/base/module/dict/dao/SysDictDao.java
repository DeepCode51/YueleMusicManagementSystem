package com.icss.sys.base.module.dict.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.sys.base.module.dict.entity.DictTree;
import com.icss.sys.base.module.dict.entity.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 【字典信息】数据接口
* SysDictDao与SysDictDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysDictDao extends BaseMapper<SysDict> {
    List<DictTree> getDictTree(SysDict sysDict);
}

