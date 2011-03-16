package com.aifuyun.snow.world.common;

import java.util.HashSet;
import java.util.Set;

import com.zjuh.sweet.lang.StringUtil;

public class SearchUtil {
	
	private static final String ESCAPING = "+,-,&,|,!,(,),{,},[,],^,\",~,*,?,:,\\";
	
	private static final Set<Character> ESCAPING_CHARS = new HashSet<Character>();
	
	static {
		String[] tokens = ESCAPING.split(",");
		for (String token : tokens) {
			if (token.length() == 0) {
				continue;
			}
			ESCAPING_CHARS.add(token.charAt(0));
		}
	}
	
	public static String filter(String input) {
		if (StringUtil.isEmpty(input)) {
			return input;
		}
		StringBuilder sb = new StringBuilder();
		char[] chars = input.toCharArray();
		for (char c : chars) {
			if (ESCAPING_CHARS.contains(c)) {
				sb.append("\\");
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String s = SearchUtil.filter("hello\"&abcd?");
		System.out.println(s);
	}

}
