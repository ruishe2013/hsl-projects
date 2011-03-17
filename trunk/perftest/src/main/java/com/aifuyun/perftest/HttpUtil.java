package com.aifuyun.perftest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	 public static void requestUrl(String urlString) {
		 try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			InputStream is = conn.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			io(is, bos);
			is.close();
			bos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	 }
	 
	 private static final int BUF_LEN = 8 * 1024;
	 
	 public static void io(InputStream is, OutputStream os) throws IOException {
		 byte[] buf = new byte[BUF_LEN];
		 while (true) {
			int len = is.read(buf);
			if (len < 0) {
				break;
			}
			os.write(buf, 0, len);
		 }
	 }
	
}
