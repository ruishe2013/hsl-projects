package com.htc.dao.impl;

import java.util.List;

import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForEqTypeCount;
import com.htc.domain.EquipData;
import com.htc.dao.iface.EquipDataDao;

public class EquipDataSqlMapDao extends BaseSqlMapDao implements EquipDataDao {

	private String nameplace = "EquipData.";

	public EquipDataSqlMapDao() {
	}
	
//	public EquipDataSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	// useless=1:�������õ�����		useless=0:�������õ�����(������ɾ����)
	
	
	// ������������ͳ�Ƹ����͵�����,���Ե������岼����
	@SuppressWarnings("unchecked")
	public List<BeanForEqTypeCount> getEqCountByType() throws Exception{
		return (List<BeanForEqTypeCount>) getSqlMapClientTemplate().queryForList(nameplace + "selectEqCountByType");
	} 

	@SuppressWarnings("unchecked")
	public List<EquipData> findEquipData(EquipData equipData) throws Exception {
		return (List<EquipData>) getSqlMapClientTemplate().queryForList(nameplace + "selectEquipData",equipData);
	}

	public void insertEquipData(EquipData equipData) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertEquipData", equipData);
	}
	
	// ��������ʱ,���ô˷���
	public void insertEquipDataBatch(List<EquipData> equipDataList)throws Exception {
		// �������
		int batch = 0;//��������
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//��ʼ����
		for (EquipData equipData : equipDataList) {
			getSqlMapClientTemplate().insert(nameplace + "insertEquipData",equipData);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//�ύ����		
	}

	public void updateEquipData(EquipData equipData) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateEquipData", equipData);
	}
	
	public void updateEquipDev(EquipData equipData) throws Exception{
		getSqlMapClientTemplate().update(nameplace + "updateEquiDev", equipData);
	}

	public void deleteEquipData(int equipmentId) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateUseless", equipmentId);//��ɾ����ֻ������
		//delete(nameplace + "deleteEquipDataById", equipmentId);
	}

	public void deleteBatch(int[] addresss) throws Exception {
		if (addresss.length > 0) {
			getSqlMapClientTemplate().getSqlMapClient().startBatch();
			int batch = 0;			//��������
			for (int addr : addresss) {
				deleteEquipData(addr);
				batch++;			// ÿ500�������ύһ�Ρ� 				
				if(batch == BATCH_SIZE){ 
					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
					batch = 0; 
				} 
			}
			getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		}
	}

	@SuppressWarnings("unchecked")
	public List<EquipData> selcetEquiOrderStr(BeanForEqOrder eqorderBean) throws Exception {
		return (List<EquipData>) getSqlMapClientTemplate().queryForList(nameplace + "selcetEquiOrderStr", eqorderBean);
	}

	public void updateEquiOrder(List<EquipData> equipDatas) throws Exception {
		if (equipDatas.size() > 0) {
			getSqlMapClientTemplate().getSqlMapClient().startBatch();
			int batch = 0;			//��������
			for (EquipData equipData : equipDatas) {
				getSqlMapClientTemplate().update(nameplace + "updateEquiOrder", equipData);
				batch++;			// ÿ500�������ύһ�Ρ� 				
				if(batch == BATCH_SIZE){ 
					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
					batch = 0; 
				} 
			}
			getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		}
	}

}
