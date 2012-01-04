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
	
	// useless=1:所有有用的区域		useless=0:所有有用的区域(包括被删掉的)
	
	
	// 根据仪器类型统计各类型的数量,用以调整整体布局用
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
	
	// 导入配置时,调用此方法
	public void insertEquipDataBatch(List<EquipData> equipDataList)throws Exception {
		// 填充数据
		int batch = 0;//批量基数
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//开始批量
		for (EquipData equipData : equipDataList) {
			getSqlMapClientTemplate().insert(nameplace + "insertEquipData",equipData);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//提交批量		
	}

	public void updateEquipData(EquipData equipData) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateEquipData", equipData);
	}
	
	public void updateEquipDev(EquipData equipData) throws Exception{
		getSqlMapClientTemplate().update(nameplace + "updateEquiDev", equipData);
	}

	public void deleteEquipData(int equipmentId) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateUseless", equipmentId);//不删除，只是隐藏
		//delete(nameplace + "deleteEquipDataById", equipmentId);
	}

	public void deleteBatch(int[] addresss) throws Exception {
		if (addresss.length > 0) {
			getSqlMapClientTemplate().getSqlMapClient().startBatch();
			int batch = 0;			//批量基数
			for (int addr : addresss) {
				deleteEquipData(addr);
				batch++;			// 每500条批量提交一次。 				
				if(batch == BATCH_SIZE){ 
					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
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
			int batch = 0;			//批量基数
			for (EquipData equipData : equipDatas) {
				getSqlMapClientTemplate().update(nameplace + "updateEquiOrder", equipData);
				batch++;			// 每500条批量提交一次。 				
				if(batch == BATCH_SIZE){ 
					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
					batch = 0; 
				} 
			}
			getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		}
	}

}
