package com.icss.sys.base.cache.service;

import com.icss.sys.base.cache.utils.EhCacheUtils;
import com.icss.sys.base.module.config.entity.SysConfig;
import com.icss.sys.base.module.config.service.SysConfigService;
import com.icss.sys.base.module.dict.entity.SysDict;
import com.icss.sys.base.module.dict.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载缓存
 */
@Service
public class SysCacheService{
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysDictService sysDictService;
    public static Map<String,Object> configMap = new HashMap<>();
    public static Map<String,Object> dictMap = new HashMap<>();

    public static final String SYS_DICT = "SYS_DICT";
    public static final String SYS_CONFIG = "SYS_CONFIG";

    public void init(){
        initConfigCache();
        initDictCache();
    }
    //初始化数据库配置
    public Map<String,Object> initConfigCache() {
        List<SysConfig> list = sysConfigService.getList(null);
        if (list.size() > 0) {
            for (SysConfig config : list) {
                configMap.put(config.getCode(), config.getVal());
            }
        }
        EhCacheUtils.put(SYS_CONFIG, configMap);
        return configMap;
    }

    //设置数据字典缓存
    public Map<String, Object> initDictCache() {
        SysDict sysDict = new SysDict();
        sysDict.setType("dict");
        List<SysDict> list = sysDictService.getList(sysDict);
        for (SysDict dict : list) {
            SysDict dictF = new SysDict();
            dictF.setPid(dict.getId());
            List<SysDict> dictCode = sysDictService.getList(dictF);
            dictMap.put(dict.getValue(), dictCode);
        }
        EhCacheUtils.put(SYS_DICT, dictMap);
        return dictMap;
    }
}
