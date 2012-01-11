package com.htc.bean.query;

import java.util.Date;

public class HistoryRecordQuery extends BaseQuery {

	private static final long serialVersionUID = 1064633682415585852L;

	private Date startTime;

	private Date endTime;

	private Integer placeId;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}


}
