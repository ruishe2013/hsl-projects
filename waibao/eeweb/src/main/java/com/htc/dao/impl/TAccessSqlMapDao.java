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
		
		List<Data4Access> exitData =  getlist(); // 获取已经存在的数据列表
		boolean findFlag = false;
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//批量基数
		
		for (Data4Access data : datas) {
			findFlag = false;
			for (Data4Access temp : exitData) {
				// 要操作的项目已经存在
				if (temp.getStrDSRSN().equals(data.getStrDSRSN())){
					findFlag = true;
					break;
				}
			}
			
			if (findFlag == false){ // 没有找到就增加到数据库
				insertData(data);
			}else{// 找到就更新
				updateData(data);
			}
			
//			batch++;			// 每500条批量提交一次。 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
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
//		int batch = 0;//批量基数
		for (Data4Access data : datas) {
			updateData(data);
//			batch++;			// 每500条批量提交一次。 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
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
		int batch = 0;//批量基数
		for (String dsrcs : strDSRSNs) {
			deleteDataById(dsrcs);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
}
