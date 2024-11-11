package com.icss.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icss.entity.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 【后台用户】数据接口
* SysUserDao与SysUserDaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface SysUserDao extends BaseMapper<SysUser> {
    //自定义分页查询
    IPage<SysUser> findByPage(Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);
    //自定义列表查询
    List<SysUser> findList(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);
}

