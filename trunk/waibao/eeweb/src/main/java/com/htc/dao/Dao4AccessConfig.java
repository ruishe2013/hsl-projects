package com.htc.dao;

import java.io.Reader;
import java.util.Properties;
import com.ibatis.dao.client.*;
import com.ibatis.common.resources.Resources;

/**
 * @ DaoConfig.java
 * 作用 : 所有到的父类
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class Dao4AccessConfig {

	private static final String resource = "com/htc/dao/dao4Access.xml";
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
		} catch (Exception e) {
			throw new RuntimeException(
					"Could not initialize DaoConfig.  Cause: " + e, e);
		}
	}

}
