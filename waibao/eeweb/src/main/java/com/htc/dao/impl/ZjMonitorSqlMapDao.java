package com.htc.dao.impl;

import java.util.List;

import com.htc.dao.iface.ZjMonitorDao;
import com.htc.domain.Workplace;
import com.htc.domain.ZjMonitors;

public class ZjMonitorSqlMapDao extends BaseSqlMapDao implements ZjMonitorDao {
	
	private String nameplace = "ZjMonitors.";
	
	public ZjMonitorSqlMapDao() {
	}

	public int test4Link() throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject(nameplace + "select4TestLink");
	}
	
	@SuppressWarnings("unchecked")
	public List<ZjMonitors> selectAllZjMonitor() throws Exception {
		return (List<ZjMonitors>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllZjMonitor");
	}
	
	public void insertZjMonitor(ZjMonitors zjMoni) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertZjMonitors", zjMoni);
	}

	public void updateMonitor(ZjMonitors zjMoni) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateZjMonitors", zjMoni);		
	}
	
	public void deleteMonitor(ZjMonitors zjMoni) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteZjMonitors", zjMoni);		
	}
	
	public void doBatchMonitor(List<ZjMonitors> zjMoniList, int batchType)throws Exception {
		int batch = 0;//�������� 
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//��ʼ����
		for (ZjMonitors zjMoni : zjMoniList) {
			switch (batchType) {
			case BaseSqlMapDao.BATCH_INSERT:
				insertZjMonitor(zjMoni);
				break;
			case BaseSqlMapDao.BATCH_UPDATE:
				updateMonitor(zjMoni);
				break;
			case BaseSqlMapDao.BATCH_DELETE:
				deleteMonitor(zjMoni);
				break;
			}
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//�ύ����			
	}
	
}
