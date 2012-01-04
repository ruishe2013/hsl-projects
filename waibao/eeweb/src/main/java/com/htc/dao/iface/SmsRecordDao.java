package com.htc.dao.iface;

import java.util.List;

import com.htc.domain.SmsRecord;

public interface SmsRecordDao {

	public List<SmsRecord> searchSmsRecs(SmsRecord searchBean) throws Exception;
	
	public void insertSmsRec(SmsRecord smsRec) throws Exception;
	
}
