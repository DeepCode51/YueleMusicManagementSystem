package com.icss.sys.base.module.gen.dao;


import com.icss.sys.base.module.gen.entity.Gen;
import com.icss.sys.base.module.gen.entity.GenTableColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */
@Repository
public interface GenMysqlDao{
    List<Gen> findList(Gen gen);
    Gen get(Gen gen);
    List<Gen> getCloums(Gen gen);
    List<GenTableColumn> findColumnList(GenTableColumn genTableColumn);
    int insert(GenTableColumn genTableColumn);
    int deleteByTableName(String tableName);
}
