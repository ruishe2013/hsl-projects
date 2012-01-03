package com.zjuh.waibao.pxsearch.dataprovider.cource;

import java.util.Date;

import com.aifuyun.search.build.TimeSupport;

public class IncrCourseDataProvider extends BaseCourseDataProvider implements TimeSupport {

	Date start;
	
	Date end;
	
	@Override
	public void setTime(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected String getSqlCondition() {
		return " 1 = 2 " ;
	}  

}
