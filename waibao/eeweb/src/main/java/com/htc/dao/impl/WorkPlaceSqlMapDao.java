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
	
	// useless=1:所有有用的区域		useless=0:所有有用的区域(包括被删掉的)
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

	// 导入配置时,调用此方法
	public void insertworkplaceBatch(List<Workplace> worklist)throws Exception {
		// 先清空数据库
		getSqlMapClientTemplate().delete(nameplace + "truncateEquipdata");// 先清理仪器表
		getSqlMapClientTemplate().delete(nameplace + "truncateWorkplace");// 再清理区域表
		
		// 填充数据
		int batch = 0;//批量基数 
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//开始批量
		for (Workplace workplace : worklist) {
			getSqlMapClientTemplate().insert(nameplace + "insertWorkplaceWithId",workplace);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//提交批量
	}
	
	public void updateWorkplace(Workplace workplace) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateWorkplace",workplace);
	}

	public void deleteWorkplace(String placeName) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateUseless",placeName);//不删除，只是隐藏
		//delete(nameplace + "deleteWorkplaceByName", placeName);
	}

	public void deleteBatch(String[] names) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (String str : names) {
			deleteWorkplace(str);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();		
	}

	public int getChlidCount(String placeName) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject(nameplace + "getChlidCount",placeName);
	}
}
