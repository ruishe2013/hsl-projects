package com.htc.dao.impl;


//import com.ibatis.dao.client.DaoManager;
//import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

//public class BaseSqlMapDao extends SqlMapDaoTemplate {
public class BaseSqlMapDao extends SqlMapClientDaoSupport {

	/**
	 * 	Ĭ�Ϸ�ҳ��С
	 */
	public static final int PAGE_SIZE = 8;
	/**
	 * �������������
	 */
	public static final int BATCH_SIZE = 200;
	/**
	 * �������־-����
	 */
	public static final int BATCH_INSERT = 1;
	/**
	 * �������־-�޸�
	 */
	public static final int BATCH_UPDATE = 2;
	/**
	 * �������־-ɾ��
	 */
	public static final int BATCH_DELETE = 3;
	
//	public BaseSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
}

