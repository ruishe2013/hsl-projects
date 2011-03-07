package com.aifuyun.snow.search.orders;

import java.util.Date;

import com.aifuyun.search.build.TimeSupport;
import com.aifuyun.search.util.DateUtil;

public class IncrOrderDataProvider extends BaseDataProvider implements TimeSupport {

	private Date start;
	
	private Date end;
	
	@Override
	public void setTime(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected String getSqlCondition() {
		return " gmt_modified >= '"+ DateUtil.formatDate(start) 
		+"' and gmt_modified <= '"+  DateUtil.formatDate(end) + "'" ;
	}  

}
