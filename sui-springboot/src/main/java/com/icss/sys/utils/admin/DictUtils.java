/**
 * Copyright &copy; 2015-2020 <a href="http://www.suifeng.org/">jeeplus</a> All rights reserved.
 */
package com.icss.sys.utils.admin;


import com.icss.sys.base.module.dict.entity.SysDict;

import java.util.List;
import java.util.stream.Collectors;

import static com.icss.sys.base.cache.service.SysCacheService.dictMap;

/**
 * 字典工具类
 */
public class DictUtils {


	public static String getDictLabel(String dictType,String value){
		List<SysDict> dictList = (List<SysDict>) dictMap.get(dictType);
		SysDict dict = dictList.stream().filter(item -> value.equals(item.getValue())).collect(Collectors.toList()).get(0);
		return dict.getLabel();
	}

	public static String getDictValue(String dictType,String label){
		List<SysDict> dictList = (List<SysDict>) dictMap.get(dictType);
		SysDict dict = dictList.stream().filter(item -> label.equals(item.getLabel())).collect(Collectors.toList()).get(0);
		return dict.getValue();
	}

	public static List<SysDict> getDictArray(String dictType){
		List<SysDict> dictList = (List<SysDict>) dictMap.get(dictType);
		return dictList;
	}
}
