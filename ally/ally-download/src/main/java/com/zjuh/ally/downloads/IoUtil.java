package com.zjuh.ally.downloads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtil {
	
	private static final int LEN = 1024 * 8;
	
	public static void io(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[LEN];
		while (true) {
			int len = is.read(buf);
			if (len < 0) {
				break;
			}
			os.write(buf, 0, len);
		}
		is.close();
		os.close();		
	}

}
