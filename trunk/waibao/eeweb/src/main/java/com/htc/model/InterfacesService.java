package com.htc.model;

import java.util.List;

import com.htc.bean.interfaces.DataRecord;
import com.htc.bean.query.HistoryRecordQuery;
import com.htc.dao.iface.HisRecordDao;

public class InterfacesService {
	
	//private Log log = LogFactory.getLog(InterfacesService.class);
	
	private HisRecordDao hisRecordDao;

	public List<DataRecord> getHistoryDataRecords(HistoryRecordQuery historyRecordQuery) {
		return hisRecordDao.getHistoryDataRecords(historyRecordQuery);
	}

	public void setHisRecordDao(HisRecordDao hisRecordDao) {
		this.hisRecordDao = hisRecordDao;
	}

}
