package com.aifuyun.snow.world.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	
	public static final String FULL_DATE_FMT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 组合datePart的日期部分和timePart的时间成为一个新的日期
	 * @param datePart 
	 * @param timePart
	 * @return
	 */
	public static Date componentDateAndTime(Date datePart, Date timePart) {
		Calendar calDatePart = toCalendar(datePart);
		Calendar calTimePart = toCalendar(timePart);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, calDatePart.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, calDatePart.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, calDatePart.get(Calendar.DAY_OF_MONTH));
		
		cal.set(Calendar.HOUR_OF_DAY, calTimePart.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, calTimePart.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, calTimePart.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, calTimePart.get(Calendar.MILLISECOND));
		
		return cal.getTime();
	}
	
	public static Date parseDate(String input) throws ParseException {
		return parseDate(input, FULL_DATE_FMT);
	}
	
	public static Date parseDate(String input, String fmt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.parse(input);
	}
	
	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static Date toDate(Calendar cal) {
		return cal.getTime();
	}

}
