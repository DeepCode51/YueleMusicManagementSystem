package com.icss.sys.base.module.config.dto;

import com.icss.sys.base.module.config.entity.SysConfig;

import java.util.List;

public class ConfigParams {
    List<SysConfig> configs;

    public List<SysConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SysConfig> configs) {
        this.configs = configs;
    }
}