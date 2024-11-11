package com.icss.sys.base.module.common.service;

import com.icss.sys.base.module.common.dao.CommonDao;
import com.icss.sys.base.module.common.entity.Unique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//通用信息接口类
@Service
public class CommonService {

    @Autowired
    private CommonDao commonDao;

    //判断是否唯一
    public boolean unique(Unique unique) {
        int count = commonDao.unique(unique);
        return count > 0 ? false : true;
    }


}