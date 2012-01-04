package com.htc.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.htc.dao.iface.SysParamDao;
import com.htc.domain.SysParam;

public class SysParamSqlMapDao extends BaseSqlMapDao implements SysParamDao {
	
	private String nameplace = "SysParam.";
	
	public SysParamSqlMapDao() {
	}

//	public SysParamSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	@SuppressWarnings("unchecked")
	public List<SysParam> selectAllParam() throws Exception {
		return (List<SysParam>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllParam");
	}	
	
	public void updateSysParam(Map<String, String> argsMap) throws Exception {
		SysParam sysParam = new SysParam();
		
		int batch = 0;//��������
		getSqlMapClientTemplate().getSqlMapClient().startBatch();			//��ʼ����
		Set<Entry<String, String>> entrys= argsMap.entrySet();
		
        for(Entry<String, String> entry: entrys){
        	String key=(String) entry.getKey();
        	String value = (String) entry.getValue();
            sysParam.setArgsKey(key);
            sysParam.setArgsValue(value);
            getSqlMapClientTemplate().update(nameplace + "updateSysParam",sysParam);
            
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			}             
        }
		
//		for (String key : argsMap.keySet()) {
//			sysParam = null;
//			sysParam = new SysParam();
//			sysParam.setArgsKey(key);
//			sysParam.setArgsValue(argsMap.get(key));
//			update(nameplace + "updateSysParam",sysParam);
//		}
        getSqlMapClientTemplate().getSqlMapClient().executeBatch();			//�ύ����
	}
	
	public void updateSysParam(List<SysParam> sysParamList) throws Exception {
		int batch = 0;//��������
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for (SysParam sysParam : sysParamList) {
			getSqlMapClientTemplate().update(nameplace + "updateSysParam",sysParam);
			
			batch++;			// ÿ500�������ύһ�Ρ� 		
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();				
	}

}
