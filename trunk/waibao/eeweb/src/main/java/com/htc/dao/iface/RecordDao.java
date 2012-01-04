package com.htc.dao.iface;

import java.util.*;

import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.domain.*;

public interface RecordDao {

	public List<Record> selectNewItem() throws Exception;
	
	public String getEarlyTime();
	
	public String getLastTime();
	
	public List<Record> selectAll(Record record) throws Exception;
	
	public List<BeanForRecord> selectAllBean(BeanForSearchRecord beanForSearchRecord) throws Exception;
	
	public List<Record> selectAvg(Record record) throws Exception;

	public List<Record> selectMin(Record record) throws Exception;

	public List<Record> selectMax(Record record) throws Exception;
	
	public void insertRecord(Record record) throws Exception;
	
	public void insertRecordBatch(Map<Integer, Record> mapRecords) throws Exception;

	public void updateRecord(Record record) throws Exception;

	public void deleteRecord(String recTime) throws Exception;
	
	public void deleteRecordBatch(String endRecTime) throws Exception;

	public void deleteAlarmBatch(String[] recTimes) throws Exception;
	
	public void truncateRecord() throws Exception;
	
}
