package com.aifuyun.search.util;

public class PathUtil {
	
	/**
	 * 去掉path最后面的\和/字符
	 * @param path
	 * @return
	 */
	public static String trimEndsSlashForPath(String path) {
		while (path.endsWith("/") || path.endsWith("\\")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
	

}
