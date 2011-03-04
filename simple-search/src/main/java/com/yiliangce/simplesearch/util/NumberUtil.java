package com.yiliangce.simplesearch.util;

public class NumberUtil {
	
	public static float toFloat(String input, float defaultValue) {
		if (input == null || input.length() == 0) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
