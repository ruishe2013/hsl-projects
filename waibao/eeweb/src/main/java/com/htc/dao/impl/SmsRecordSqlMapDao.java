package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.SmsRecord;
import com.htc.dao.iface.SmsRecordDao;

public class SmsRecordSqlMapDao extends BaseSqlMapDao implements SmsRecordDao {
	
	private String nameplace = "smsrecord.";
	public SmsRecordSqlMapDao() {
	}

	@SuppressWarnings("unchecked")
	public List<SmsRecord> searchSmsRecs(SmsRecord searchBean) throws Exception {
		return (List<SmsRecord>)getSqlMapClientTemplate().queryForList(nameplace + "selectRecs", searchBean);
	}

	public void insertSmsRec(SmsRecord smsRec) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertRecord", smsRec);
	}
}
