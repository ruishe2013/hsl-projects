package com.htc.action.interfaces;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.action.AbstractAction;
import com.htc.bean.BeanForPortData;
import com.htc.bean.interfaces.DataRecord;
import com.htc.domain.EquipData;
import com.htc.model.MainService;
import com.htc.util.CollectionUtil;
import com.htc.util.DateUtil;

public class LastData extends AbstractAction {

	private static final long serialVersionUID = 2268263766055937865L;
	
	private static final Log log = LogFactory.getLog(LastData.class);
	
	private MainService mainService;
	
	private Collection<List<DataRecord>> lastDatas;
	
	private int resultCode = 0;
	
	private String resultMessage = "";
	
	public String execute() {
		try {
			List<BeanForPortData> tempDataBean = mainService.getNewestMinRecAll();
			Map<Integer, List<DataRecord>> tempDatas = CollectionUtil.newTreeMap();
			for (BeanForPortData bean: tempDataBean) {
				addDataRecord(tempDatas, bean);
			}
			lastDatas = tempDatas.values();
		} catch (Exception e) {
			log.error("query last data failed", e);
			resultCode = 1;
			resultMessage = "查询最新数据失败";
		}
		return SUCCESS;
	}
	
	private void addDataRecord(Map<Integer, List<DataRecord>> tempDatas, BeanForPortData beanForPortData) {
		int equId = beanForPortData.getEquipmentId();
		EquipData equipData = commonDataUnit.getEquipByID(equId);
		if (equipData != null) {
			DataRecord dataRecord = new DataRecord();
			dataRecord.setPlaceName(equipData.getPlaceStr());
			dataRecord.setAddress(String.valueOf(equipData.getAddress()));
			dataRecord.setDsrsn(equipData.getDsrsn());
			dataRecord.setHumidity(String.valueOf(beanForPortData.getHumi()));
			dataRecord.setTemperature(String.valueOf(beanForPortData.getTemp()));
			dataRecord.setRecordTime(DateUtil.formatFullDate(new Date(beanForPortData.getRecLong())));
			dataRecord.setState(String.valueOf(beanForPortData.getState()));
			int placeId = equipData.getPlaceId();
			
			List<DataRecord> records = tempDatas.get(placeId);
			if (records == null) {
				records = CollectionUtil.newArrayList();
				tempDatas.put(placeId, records);
			}
			records.add(dataRecord);
		}
		
	}

	public Collection<List<DataRecord>> getLastDatas() {
		return lastDatas;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	
}
