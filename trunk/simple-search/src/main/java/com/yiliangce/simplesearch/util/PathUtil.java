package com.yiliangce.simplesearch.util;

public class PathUtil {
	
	/**
	 * ȥ��path������\��/�ַ�
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
