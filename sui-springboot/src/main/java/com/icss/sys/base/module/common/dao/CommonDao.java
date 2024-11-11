package com.icss.sys.base.module.common.dao;

import com.icss.sys.base.module.common.entity.Unique;
import org.springframework.stereotype.Repository;

/**
*  通用信息接口
*/
@Repository
public interface CommonDao {
    //数据唯一值判断
    int unique(Unique unique);
}

