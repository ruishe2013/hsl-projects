package com.htc.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.htc.bean.BeanForMinRec;
import com.htc.bean.BeanForPortData;
import com.htc.dao.iface.MinRecordDao;

public class MinRecordSqlMapDao extends BaseSqlMapDao implements MinRecordDao {
	
	private String nameplace = "MinRecord.";
	
	public MinRecordSqlMapDao() {
	}

//	public MinRecordSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public int getCounts(){
		return (Integer)getSqlMapClientTemplate().queryForObject(nameplace + "getCounts");
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BeanForPortData> getNewestRec(String userPlaceList){
		BeanForMinRec beanMinRec = new BeanForMinRec();
		beanMinRec.setPlaceList(userPlaceList);
		beanMinRec.setRecLong(lastRecTime());// 得到最新是记录时间
		return (List<BeanForPortData>)getSqlMapClientTemplate().queryForList(nameplace + "getNewestRec", beanMinRec);
	}
	
	@SuppressWarnings("unchecked")
	public List<BeanForPortData> getNewestRecAll(){
		BeanForMinRec beanMinRec = new BeanForMinRec();
		beanMinRec.setRecLong(lastRecTime());// 得到最新是记录时间
		return (List<BeanForPortData>)getSqlMapClientTemplate().queryForList(nameplace + "getNewestRecAll", beanMinRec);
	}
	
	public long lastRecTime(){
		Object obj = getSqlMapClientTemplate().queryForObject(nameplace + "lastRecTime");
		if (obj == null){return 0;
		}else{return (Long)obj;}
	}
	
	public long earlyRecTime(){
		Object obj = getSqlMapClientTemplate().queryForObject(nameplace + "earlyRecTime");
		if (obj == null){return 0;
		}else{return (Long)obj;}
	}
	
	@SuppressWarnings("unchecked")
	public List<BeanForPortData> selectAllRec(String userPlaceList){
		return (List<BeanForPortData>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllRec", userPlaceList);
	}
	
	public void insertRecord(BeanForPortData record) {
		getSqlMapClientTemplate().insert(nameplace + "insertRecord", record);
	}
	public void insertRecord(Map<Integer, BeanForPortData> records, int repeat) throws SQLException {
		Date currentTime = new Date();
		long mill = currentTime.getTime();
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//批量基数
		
		for (int i = 0; i < repeat; i++) { // 根据repeat(重复个数),添加空数据
			Set<Entry<Integer, BeanForPortData>> entrys= records.entrySet();
			BeanForPortData record;//值
			mill = mill + 1;
			for(Entry<Integer, BeanForPortData> entry: entrys){
				record = (BeanForPortData) entry.getValue();
				record.setRecTime(currentTime);
				record.setRecLong(mill);
				insertRecord(record);
				
//				batch++;			// 每500条批量提交一次。 				
//				if(batch == BATCH_SIZE){ 
//					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
//					batch = 0; 
//				}            
			}
		}
//		getSqlMapClientTemplate().getSqlMapClient().executeBatch();		
	}
	
	public void updateRecord(BeanForPortData record){
		getSqlMapClientTemplate().update(nameplace + "updateRecord", record);
	}		
	public void updateRecord(Map<Integer, BeanForPortData> records) throws SQLException {
		Date currentTime = new Date();
		long oldmill = earlyRecTime();
		long mill = currentTime.getTime();
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//批量基数
		
		Set<Entry<Integer, BeanForPortData>> entrys= records.entrySet();
		BeanForPortData record;//值
        for(Entry<Integer, BeanForPortData> entry: entrys){
            record = null;     record = (BeanForPortData) entry.getValue();
            record.setRecTime(currentTime);
            record.setRecLong(mill);
            record.setOldrecLong(oldmill);
            updateRecord(record);

			//batch++;			// 每500条批量提交一次。 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
//				batch = 0; 
//			}            
        }
//        getSqlMapClientTemplate().getSqlMapClient().executeBatch();	
	}
	
	public void deleteAll() throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteAll");
	}

	public void truncateRecord() throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "truncateRecord");
	}

}
