package com.aifuyun.search.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.aifuyun.search.SearchException;


public class ResouceUtil {
	
	public static InputStream getResource(String resource) {
		InputStream is = ResouceUtil.class.getResourceAsStream(resource);
		if (is == null) {
			is = ResouceUtil.class.getClassLoader().getResourceAsStream(resource);
		}
		return is;
	}

	public static String getResourceContentString(String resource) throws IOException {
		InputStream is = getResource(resource);
		if (is == null) {
			return null;
		}
		StringWriter out = new StringWriter();
		try {
			IoUtil.copy(new InputStreamReader(is, "gbk"), out);
		} catch(UnsupportedEncodingException e) {
			throw new SearchException(e);
		}
		is.close();
		return out.toString();
	}
	
}
