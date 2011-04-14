package com.aifuyun.snow.world.common;

import java.io.InputStream;

public class ClassInputStreamUtil {
	
	public static InputStream getInputStream(String path) {
		InputStream is = ClassInputStreamUtil.class.getResourceAsStream(path);
		if (is != null) {
			return is;
		}
		is = ClassInputStreamUtil.class.getClassLoader().getResourceAsStream(path);
		return is;
	}

}
