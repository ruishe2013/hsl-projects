package com.aifuyun.search.date;

import java.util.Calendar;
import java.util.Date;

import com.aifuyun.search.util.DateUtil;

public class DateRange {

	private Date start;
	
	private Date end;

	public DateRange() {
		super();
	}

	public DateRange(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

	public void add(int type, int amount) {
		start = new Date(end.getTime());
		
		Calendar endCal = DateUtil.toCalendar(end);
		endCal.set(type, amount);
		end = endCal.getTime();
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
}
