package com.aifuyun.search.date;

import java.util.Date;

public interface DatetimeStore {
	
	/**
	 * 从介质中加载
	 */
	Date load(String coreName);
	
	/**
	 * 保存到介质
	 */
	void save(String coreName, Date date);
	
}
