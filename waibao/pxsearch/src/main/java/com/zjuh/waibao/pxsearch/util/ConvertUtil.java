package com.zjuh.waibao.pxsearch.util;

public class ConvertUtil {

	public static int toInt(String input, int defaultValue) {
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
}
