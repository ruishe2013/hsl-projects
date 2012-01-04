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
	
	// 导入配置时,调用此方法
	public void insertEquipDataBatch(List<GprsSet> gprsSetList)throws Exception {
		// 先清空数据库
		getSqlMapClientTemplate().delete(nameplace + "truncateGprsSet");		
		// 填充数据
		int batch = 0;//批量基数
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//开始批量
		for (GprsSet gprsSet : gprsSetList) {
			getSqlMapClientTemplate().insert(nameplace + "insertGprsSet",gprsSet);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//提交批量		
	}	

	public void updateGprsSet(GprsSet gprsSet) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateGprsSet",gprsSet);
	}

	public void deleteGprsSet(int gprsSetId) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteGprsSetById", gprsSetId);
	}
	
	public void deleteBatch(int[] gprsSetIds) throws Exception{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (int i : gprsSetIds) {
			deleteGprsSet(i);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
	
}
