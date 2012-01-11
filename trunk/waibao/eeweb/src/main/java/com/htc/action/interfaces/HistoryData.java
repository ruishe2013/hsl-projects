package com.htc.action.interfaces;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.action.AbstractAction;
import com.htc.bean.interfaces.DataRecord;
import com.htc.bean.query.HistoryRecordQuery;
import com.htc.model.InterfacesService;

public class HistoryData extends AbstractAction {
	
	private static final long serialVersionUID = 4348886524717508252L;
	
	private static final Log log = LogFactory.getLog(HistoryData.class);
	
	private InterfacesService interfacesService;
	
	private List<DataRecord> dataRecords;
	
	private int resultCode = 0;
	
	private String resultMessage = "";
	
	private Date startTime;
	
	private Date endTime;
	
	private Integer placeId;
	
	private int pageSize = 50;
	
	private int page = 1;
	
	public String execute() {
		if (page <= 1) {
			page = 1;
		}
		if (pageSize <= 1) {
			pageSize = 50;
		}
		try {
			HistoryRecordQuery historyRecordQuery = new HistoryRecordQuery();
			historyRecordQuery.setPageNo(page);
			historyRecordQuery.setPageSize(pageSize);
			historyRecordQuery.setEndTime(endTime);
			historyRecordQuery.setStartTime(startTime);
			historyRecordQuery.setPlaceId(placeId);
			dataRecords = interfacesService.getHistoryDataRecords(historyRecordQuery);
			transforDatas(dataRecords);
		} catch (Exception e) {
			log.error("查询历史数据失败", e);
			resultCode = 1;
			resultMessage = "查询历史数据失败";
		}
		return SUCCESS;
	}
	
	private void transforDatas(List<DataRecord> dataRecords) {
		for (DataRecord dr : dataRecords) {
			dr.setState("1");
		}
	}

	public List<DataRecord> getDataRecords() {
		return dataRecords;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setInterfacesService(InterfacesService interfacesService) {
		this.interfacesService = interfacesService;
	}

}
