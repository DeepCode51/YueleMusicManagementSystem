package com.icss.sys.base.module.extend.dao;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 基础dao用于service查询基础字段查询避免报错
 * 继承实体的字段查询LambdaQueryWrapper会报错
*/
@Repository
public interface BaseDao extends BaseMapper<BaseEntity> {

}

