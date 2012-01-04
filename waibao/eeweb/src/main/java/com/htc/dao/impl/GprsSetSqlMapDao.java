package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.GprsSet;
import com.htc.dao.iface.GprsSetDao;

public class GprsSetSqlMapDao extends BaseSqlMapDao implements GprsSetDao {
	
	private String nameplace = "GprsSet.";
	
	public GprsSetSqlMapDao() {
	}

//	public GprsSetSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	@SuppressWarnings("unchecked")
	public List<GprsSet> findAllSetList() throws Exception {
		return (List<GprsSet>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll");
	}		

	@SuppressWarnings("unchecked")
	public List<GprsSet> findGprsSet(GprsSet gprsSet) throws Exception {
		return (List<GprsSet>)getSqlMapClientTemplate().queryForList(nameplace + "selectGprsSet",gprsSet);
	}

	public void insertGprsSet(GprsSet gprsSet) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertGprsSet",gprsSet);
	}
	
	// ��������ʱ,���ô˷���
	public void insertEquipDataBatch(List<GprsSet> gprsSetList)throws Exception {
		// ��������ݿ�
		getSqlMapClientTemplate().delete(nameplace + "truncateGprsSet");		
		// �������
		int batch = 0;//��������
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//��ʼ����
		for (GprsSet gprsSet : gprsSetList) {
			getSqlMapClientTemplate().insert(nameplace + "insertGprsSet",gprsSet);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//�ύ����		
	}	

	public void updateGprsSet(GprsSet gprsSet) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateGprsSet",gprsSet);
	}

	public void deleteGprsSet(int gprsSetId) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteGprsSetById", gprsSetId);
	}
	
	public void deleteBatch(int[] gprsSetIds) throws Exception{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//��������
		for (int i : gprsSetIds) {
			deleteGprsSet(i);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
	
}
