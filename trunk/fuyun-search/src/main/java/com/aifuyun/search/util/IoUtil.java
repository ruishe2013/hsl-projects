package com.aifuyun.search.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IoUtil {
	
	private static final int BUF_SIZE = 8 * 1024;
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		int len = 0;
		while ((len = is.read()) >=0) {
			os.write(buf, 0, len);
		}
	}
	
	public static void copy(Reader reader, Writer out) throws IOException {
		char[] buf = new char[BUF_SIZE];
		int len = 0;
		while ((len = reader.read(buf)) >=0) {
			out.write(buf, 0, len);
		}
	}

}
