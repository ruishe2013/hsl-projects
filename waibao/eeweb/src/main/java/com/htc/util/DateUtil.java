package com.htc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

	public static String formatDate(Object date, String fmt) {
		if (date == null) {
			return null;
		}
		if (!(date instanceof Date)) {
			return date.toString();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}

	public static Date parseDate(String date) {
		return parseDate(date, DEFAULT_DATE_FMT);
	}

	public static String formatFullDate(Object date) {
		return formatDate(date, DEFAULT_DATE_FMT);
	}

	public static Date parseDate(String date, String fmt) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
