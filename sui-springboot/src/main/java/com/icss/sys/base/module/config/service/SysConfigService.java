package com.icss.sys.base.module.config.service;

import com.icss.sys.base.cache.service.SysCacheService;
import com.icss.sys.base.module.config.dao.SysConfigDao;
import com.icss.sys.base.module.config.entity.SysConfig;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//系统配置信息接口类
@Service
public class SysConfigService extends BaseService {

    @Autowired
    private SysConfigDao sysConfigDao;

    //【系统配置信息】设置查询条件
    private LambdaQueryWrapper<SysConfig> getMenuQueryCondition(SysConfig sysConfig) {
        LambdaQueryWrapper<SysConfig> lambdaQuery =this.getBaseQueryCondition(sysConfig);
        //根据创建时间排序
        lambdaQuery.orderByDesc(SysConfig::getCreateDate);
        if (ObjectUtils.isNotNull(sysConfig)) {
            //【配置名称】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysConfig.getName()), SysConfig::getName, sysConfig.getName());
            //【配置取值】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysConfig.getVal()), SysConfig::getVal, sysConfig.getVal());
            //【配置编码】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysConfig.getCode()), SysConfig::getCode, sysConfig.getCode());
        }
        return lambdaQuery;
    }


    //【系统配置信息】分页查询
    public IPage<SysConfig> getPage(Page<SysConfig> page, SysConfig sysConfig) {
        LambdaQueryWrapper<SysConfig> lambdaQuery = getMenuQueryCondition(sysConfig);
        return sysConfigDao.selectPage(page, lambdaQuery);
    }
    
    //【系统配置信息】查询列表
    public List<SysConfig> getList(SysConfig sysConfig) {
        LambdaQueryWrapper<SysConfig> lambdaQuery = getMenuQueryCondition(sysConfig);
        return sysConfigDao.selectList(lambdaQuery);
    }
    
    //【系统配置信息】根据id查询
    public SysConfig get(String id) {
        return sysConfigDao.selectById(id);
    }
    //【系统配置信息】根据对象查询
    public SysConfig get(SysConfig params) {
        LambdaQueryWrapper<SysConfig> lambdaQuery = getMenuQueryCondition(params);
        return sysConfigDao.selectOne(lambdaQuery);
    }

    //【系统配置信息】新增
    public int insert(SysConfig sysConfig) {
        sysConfig.preInsert();
        return sysConfigDao.insert(sysConfig);
    }
    
    //【系统配置信息】修改
    public int update(SysConfig sysConfig) {
        sysConfig.preUpdate();
        return sysConfigDao.updateById(sysConfig);
    }
    
    //【系统配置信息】删除
    public int delete(String id) {
        return sysConfigDao.deleteById(id);
    }

    //【系统配置信息】批量删除
    public int delAll(String[] ids) {
        return sysConfigDao.deleteBatchIds(Arrays.asList(ids));
    }

    //获取配置
    public static String getSysConfig(String key) {
        Map<String,Object> configMap = SysCacheService.configMap;
        if (configMap != null) {
            return (String) configMap.get(key);
        }
        return null;
    }

}