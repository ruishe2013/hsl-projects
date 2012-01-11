package com.htc.dao.iface;

import java.util.List;

import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.bean.interfaces.DataRecord;
import com.htc.bean.query.HistoryRecordQuery;
import com.htc.domain.HisRecord;

public interface HisRecordDao {
	
	//stattype 1:代表日报类型	 2:代表月报类型

	public String getEarlyTime(int stattype);
	
	public String getLastTime(int stattype);
	
	public List<BeanForRecord> selectStatItmes(BeanForSearchRecord beanForSearchRecord) throws Exception;

	public void insertHisRecord(HisRecord hisRecord) throws Exception;

	public void updateHisRecord(HisRecord hisRecord) throws Exception;

	public void deleteHisRecord(int recId, int stattype) throws Exception;

	public void deleteHisRecordBatch(int[] recIds, int stattype) throws Exception;
	
	public List<DataRecord> getHistoryDataRecords(HistoryRecordQuery historyRecordQuery);
	

}
