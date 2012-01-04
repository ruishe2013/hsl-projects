package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.Workplace;
import com.htc.dao.iface.WorkPlaceDao;

public class WorkPlaceSqlMapDao extends BaseSqlMapDao implements WorkPlaceDao {
	
	private String nameplace = "Workplace.";
	
	public WorkPlaceSqlMapDao() {
	}

//	public WorkPlaceSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	// useless=1:�������õ�����		useless=0:�������õ�����(������ɾ����)
	@SuppressWarnings("unchecked")
	public List<Workplace> findAllWorkplace(int useless) throws Exception {
		return (List<Workplace>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll", useless);
	}	
	
	public Workplace findWorkplace(Workplace workplace) throws Exception {
		return (Workplace)getSqlMapClientTemplate().queryForObject(nameplace + "selectWorkplace",workplace);
	}

	public void insertWorkplace(Workplace workplace) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertWorkplace",workplace);
	}

	// ��������ʱ,���ô˷���
	public void insertworkplaceBatch(List<Workplace> worklist)throws Exception {
		// ��������ݿ�
		getSqlMapClientTemplate().delete(nameplace + "truncateEquipdata");// ������������
		getSqlMapClientTemplate().delete(nameplace + "truncateWorkplace");// �����������
		
		// �������
		int batch = 0;//�������� 
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//��ʼ����
		for (Workplace workplace : worklist) {
			getSqlMapClientTemplate().insert(nameplace + "insertWorkplace",workplace);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//�ύ����
	}
	
	public void updateWorkplace(Workplace workplace) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateWorkplace",workplace);
	}

	public void deleteWorkplace(String placeName) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateUseless",placeName);//��ɾ����ֻ������
		//delete(nameplace + "deleteWorkplaceByName", placeName);
	}

	public void deleteBatch(String[] names) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//��������
		for (String str : names) {
			deleteWorkplace(str);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();		
	}

	public int getChlidCount(String placeName) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject(nameplace + "getChlidCount",placeName);
	}
}
