package com.htc.dao.impl;

import java.util.*;
import java.util.Map.Entry;

import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.dao.iface.RecordDao;

public class RecordSqlMapDao extends BaseSqlMapDao implements RecordDao {
	
	private String nameplace = "Record.";
	
	public RecordSqlMapDao() {
	}

//	public RecordSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public String getEarlyTime(){
		return (String) getSqlMapClientTemplate().queryForObject(nameplace + "getEarlyTime");
	}
	
	public String getLastTime(){
		return (String) getSqlMapClientTemplate().queryForObject(nameplace + "getLastTime");
	}
	@SuppressWarnings("unchecked")
	public List<Record> selectNewItem() throws Exception {
		return (List<Record>)getSqlMapClientTemplate().queryForList(nameplace + "selectNewItem");
	}
	@SuppressWarnings("unchecked")
	public List<Record> selectAll(Record record){
		return (List<Record>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll",record);
	}
	@SuppressWarnings("unchecked")
	public List<BeanForRecord> selectAllBean(BeanForSearchRecord beanForSearchRecord){ 
		return (List<BeanForRecord>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllBean",beanForSearchRecord);
	}

	@SuppressWarnings("unchecked")
	public List<Record> selectAvg(Record record){
		return (List<Record>)getSqlMapClientTemplate().queryForList(nameplace + "selectAvg",record);
	}
	
	@SuppressWarnings("unchecked")
	public List<Record> selectMin(Record record){
		return (List<Record>)getSqlMapClientTemplate().queryForList(nameplace + "selectMin",record);
	}
	
	@SuppressWarnings("unchecked")
	public List<Record> selectMax(Record record){
		return (List<Record>)getSqlMapClientTemplate().queryForList(nameplace + "selectMax",record);
	}
	
	public void insertRecord(Record record) {
		getSqlMapClientTemplate().insert(nameplace + "insertRecord", record);
	}
	
	public void insertRecordBatch(Map<Integer, Record> mapRecords) throws Exception{
		// 舍去秒数
		Date currentTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_MINUTE).getTime();
		int batch = 0;//批量基数
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		
		Set<Entry<Integer, Record>> entrys= mapRecords.entrySet();
		Record record;//值
		for(Entry<Integer, Record> entry: entrys){
			record = null;     record = (Record) entry.getValue();
			record.setRecTime(currentTime);
			insertRecord(record);
			
			batch++;			// 每500条批量提交一次。 		BATCH_SIZE		
			if(batch == 3){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			}            
		}
		
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

	public void updateRecord(Record record) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateRecord", record);
	}			
	
	public void deleteRecord(String recTime) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteRecordByTime", recTime);
	}

	public void deleteRecordBatch(String endRecTime) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteRecordBatch", endRecTime);
	}
	
	public void deleteAlarmBatch(String[] recTimes) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (String recTime : recTimes) {
			deleteRecord(recTime);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	
	}

	public void truncateRecord() throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "truncateRecord");
	}

}
