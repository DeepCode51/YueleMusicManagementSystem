package com.icss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.Register;
import org.springframework.stereotype.Repository;

/**
* 【前台用户】数据接口
* UserRegisterDao与UserRegisterDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface RegisterDao extends BaseMapper<Register> {

}

