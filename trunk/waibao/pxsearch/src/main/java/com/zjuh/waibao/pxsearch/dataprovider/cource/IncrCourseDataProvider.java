package com.zjuh.waibao.pxsearch.dataprovider.cource;

import java.util.Date;

import com.aifuyun.search.build.TimeSupport;
import com.aifuyun.search.util.DateUtil;

public class IncrCourseDataProvider extends BaseCourseDataProvider implements TimeSupport {

	private Date start;
	
	private Date end;
	
	@Override
	public void setTime(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected String getSqlCondition() {
		return " t.gmt_modified >= '"+ DateUtil.formatDate(start) 
		+"' and t.gmt_modified <= '"+  DateUtil.formatDate(end) + "'" ;
	}  

}
