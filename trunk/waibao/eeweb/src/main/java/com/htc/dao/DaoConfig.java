package com.htc.dao;

import java.io.Reader;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;

/**
 * @ DaoConfig.java
 * 作用 : 所有到的父类
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class DaoConfig {

	private static final String resource = "com/htc/dao/dao.xml";
	private static final DaoManager daoManager;

	static {
		try {
			daoManager = newDaoManager(null);
		} catch (Exception e) {
			throw new RuntimeException("Description.  Cause: " + e, e);
		}
	}

	public static DaoManager getDaoManager() {
		return daoManager;
	}

	public static DaoManager newDaoManager(Properties props) {
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			return DaoManagerBuilder.buildDaoManager(reader, props);
		} catch (Throwable e) {
			throw new RuntimeException(
					"Could not initialize DaoConfig.  Cause: " + e, e);
		}
	}

}
