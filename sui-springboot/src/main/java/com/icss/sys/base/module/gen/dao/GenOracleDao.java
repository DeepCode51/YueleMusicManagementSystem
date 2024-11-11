package com.icss.sys.base.module.gen.dao;


import com.icss.sys.base.module.gen.entity.Gen;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */
@Repository
public interface GenOracleDao {
    List<Gen> getCloums(Gen gen);
    Gen get(String id);
    List<Gen> findList(Gen gen);
    int insert(Gen log);
    int update(Gen log);
    int delete(Gen log);
}
