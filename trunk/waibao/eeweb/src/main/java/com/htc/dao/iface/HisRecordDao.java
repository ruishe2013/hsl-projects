package com.htc.dao.iface;

import java.util.List;

import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.domain.*;

public interface HisRecordDao {
	
	//stattype 1:�����ձ�����	 2:�����±�����

	public String getEarlyTime(int stattype);
	
	public String getLastTime(int stattype);
	
	public List<BeanForRecord> selectStatItmes(BeanForSearchRecord beanForSearchRecord) throws Exception;

	public void insertHisRecord(HisRecord hisRecord) throws Exception;

	public void updateHisRecord(HisRecord hisRecord) throws Exception;

	public void deleteHisRecord(int recId, int stattype) throws Exception;

	public void deleteHisRecordBatch(int[] recIds, int stattype) throws Exception;
	

}
