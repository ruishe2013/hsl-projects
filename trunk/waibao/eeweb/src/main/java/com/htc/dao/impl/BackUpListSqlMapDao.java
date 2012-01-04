package com.htc.dao.impl;

import com.htc.dao.iface.BackUpListDao;
import com.htc.domain.BackUpList;

public class BackUpListSqlMapDao extends BaseSqlMapDao implements BackUpListDao {
	
	private String nameplace = "BackUpList.";
	
	public BackUpListSqlMapDao() {
	}

//	public BackUpListSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public BackUpList findBackUpList(BackUpList backUpList) throws Exception {
		
		return (BackUpList)getSqlMapClientTemplate().queryForObject(nameplace + "selectBackUpList", backUpList);
	}
	
	public String getLastTime()throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject(nameplace + "getLastTime");
	}	

	public void insertBackUpList(BackUpList backUpList) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertBackUp",backUpList);
	}
	
	public void updateBackUpList(BackUpList backUpList) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateBackUp",backUpList);
	}

	public void deleteBackUpListById(int backId) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteBackUpById", backId);
	}
	
	public void deleteBatch(int[] backIds) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (int intTemp : backIds) {
			deleteBackUpListById(intTemp);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

}
