package com.htc.model.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ CSVBackUp.java
 * 作用 : 把每个月的数据备份成csv格式保存 -- 不用
 * 注意事项 : 暂时不用(用excel) 
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class CSVBackUp {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(CSVBackUp.class);
	
	public static void ReStoreToFile(String host, String DBName, String name, String key,
			String table, String file, String append) {
	}
	

/*	public static void main(String[] ages) {
		long start = System.currentTimeMillis();
		System.out.println("start...");
	
		start = System.currentTimeMillis() - start;
		System.out.println("运行:" + start + "毫秒");
		System.out.println("/** OK! /");
	}*/

}
