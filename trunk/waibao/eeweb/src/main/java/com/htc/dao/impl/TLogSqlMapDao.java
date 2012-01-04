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
		int batch = 0;//��������
		for (int id : ids) {
			deleteLogById(id);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
}
