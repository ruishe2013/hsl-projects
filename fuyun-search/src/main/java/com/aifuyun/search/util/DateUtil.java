package com.aifuyun.search.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String DEFALT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DUMP_DATE_FMT = "yyyyMMddHHmmss";
	
	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date todayStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, DEFALT_DATE_FMT);
	}
	
	public static String formatDateForDump(Date date) {
		if (date == null) {
			return "";
		}
		return formatDate(date, DUMP_DATE_FMT);
	}
	
	public static String formatDate(Date date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}
	
	public static Date parseDate(String dateString, Date defaultDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFALT_DATE_FMT);
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return defaultDate;
		}
	}

}
