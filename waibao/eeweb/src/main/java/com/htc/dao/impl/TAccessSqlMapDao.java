package com.htc.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.htc.domain.*;
import com.htc.dao.iface.TAccessDao;

public class TAccessSqlMapDao extends BaseSqlMapDao implements TAccessDao {

	private String nameplace = "TAccess.";

	public TAccessSqlMapDao() {
	}
	
//	public TAccessSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	@SuppressWarnings("unchecked")
	public List<Data4Access> getlist(){
		return  (List<Data4Access>)getSqlMapClientTemplate().queryForList(nameplace + "getList");
	}
	
	public void insertData(Data4Access data4Access){
		getSqlMapClientTemplate().insert(nameplace + "insertData", data4Access);
	}

	public void insertBatch(List<Data4Access> datas) throws SQLException{
		
		List<Data4Access> exitData =  getlist(); // ��ȡ�Ѿ����ڵ������б�
		boolean findFlag = false;
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//��������
		
		for (Data4Access data : datas) {
			findFlag = false;
			for (Data4Access temp : exitData) {
				// Ҫ��������Ŀ�Ѿ�����
				if (temp.getStrDSRSN().equals(data.getStrDSRSN())){
					findFlag = true;
					break;
				}
			}
			
			if (findFlag == false){ // û���ҵ������ӵ����ݿ�
				insertData(data);
			}else{// �ҵ��͸���
				updateData(data);
			}
			
//			batch++;			// ÿ500�������ύһ�Ρ� 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
//				batch = 0; 
//			} 				
		}
//		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}	
	
	
	public void updateData(Data4Access data4Access) {
		getSqlMapClientTemplate().insert(nameplace + "updateData", data4Access);
	}	
	
	public void updateBatch(List<Data4Access> datas) throws SQLException {
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//��������
		for (Data4Access data : datas) {
			updateData(data);
//			batch++;			// ÿ500�������ύһ�Ρ� 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
//				batch = 0; 
//			} 				
		}
//		getSqlMapClientTemplate().getSqlMapClient().executeBatch();		
	}	
	
	public void deleteAll() {
		getSqlMapClientTemplate().delete(nameplace + "deleteAll");
	}
	
	public void deleteDataById(String strDSRSN) {
		getSqlMapClientTemplate().delete(nameplace + "deletetData", strDSRSN);
	}

	public void deleteBatch(String[] strDSRSNs) throws SQLException {	
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//��������
		for (String dsrcs : strDSRSNs) {
			deleteDataById(dsrcs);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
}
