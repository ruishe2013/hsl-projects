package com.zjuh.waibao.pxsearch.util;

public class HtmlUtil {
	
	public static String removeHtmlTags(String htmlString) {
		if (htmlString == null) {
			return "";
		}
        String text = htmlString.replaceAll("\\<.*?\\>", " ");
        return text;
    }
	
}
