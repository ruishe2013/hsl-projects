package com.zjuh.ally.downloads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Downloader {

	private static String getBaseName() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_");
		return df.format(new Date());
	}
	
	public static int downloadImages(String urlString, String destPath) throws IOException {
		List<String> images = ImageUrlParser.imageUrl(urlString);
		File dir = new File(destPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String baseName = getBaseName();
		int count = 0;
		for (String imageUrl : images) {
			++count;
			File imageFile = new File(dir, baseName + count + ".jpg");
			FileOutputStream fos = new FileOutputStream(imageFile);
			InputStream is = getHttpInputStream(imageUrl);
			IoUtil.io(is, fos);
		}
		
		return count;
	}
	
	private static InputStream getHttpInputStream(String imageUrl) throws IOException {
		URL url = new URL(imageUrl);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		return conn.getInputStream();
	}
	
}
