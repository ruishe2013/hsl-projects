package com.aifuyun.search.date;

import java.util.Date;

public interface DatetimeStore {
	
	/**
	 * �ӽ����м���
	 */
	Date load(String coreName);
	
	/**
	 * ���浽����
	 */
	void save(String coreName, Date date);
	
}
