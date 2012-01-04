package com.htc.dao.impl;

import com.htc.domain.*;
import com.htc.dao.iface.TLogDao;

public class TLogSqlMapDao extends BaseSqlMapDao implements TLogDao {

	private String nameplace = "TLog.";
	
	public TLogSqlMapDao() {
	}

//	public TLogSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public void insertLog(TLog tlog) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertTLog", tlog);
	}

	public void deleteLogById(int id) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deletetlog", id);
	}

	public void deleteLogBatch(int[] ids) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (int id : ids) {
			deleteLogById(id);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
}
