package com.htc.dao.impl;


//import com.ibatis.dao.client.DaoManager;
//import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

//public class BaseSqlMapDao extends SqlMapDaoTemplate {
public class BaseSqlMapDao extends SqlMapClientDaoSupport {

	/**
	 * 	默认分页大小
	 */
	public static final int PAGE_SIZE = 8;
	/**
	 * 最大批处理数量
	 */
	public static final int BATCH_SIZE = 200;
	/**
	 * 批处理标志-插入
	 */
	public static final int BATCH_INSERT = 1;
	/**
	 * 批处理标志-修改
	 */
	public static final int BATCH_UPDATE = 2;
	/**
	 * 批处理标志-删除
	 */
	public static final int BATCH_DELETE = 3;
	
//	public BaseSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
}

