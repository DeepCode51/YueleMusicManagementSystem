package com.icss.sys.utils.admin;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类
 */
public class IdGen {
	private static SecureRandom random = new SecureRandom();
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static String primaryKey() {
		return IdWorker.getId()+"";
	}
}
