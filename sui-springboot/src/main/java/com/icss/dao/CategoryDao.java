package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.Category;
import org.springframework.stereotype.Repository;

/**
* 【类别信息】数据接口
* CategoryDao与CategoryDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface CategoryDao extends BaseMapper<Category> {

}

