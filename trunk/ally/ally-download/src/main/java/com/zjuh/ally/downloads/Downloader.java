package com.zjuh.ally.downloads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class Downloader {

	private static String getBaseName() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		return df.format(new Date());
	}
	
	public static int downloadImages(String urlString, String destPath) throws IOException {
		Collection<String> images = ImageUrlParser.imageUrl(urlString);
		File dir = new File(destPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String baseName = getBaseName();
		File targetDir = new File(dir, baseName);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		int count = 0;
		for (String imageUrl : images) {
			File imageFile = new File(targetDir, (count + 1) + ".jpg");
			InputStream is = getHttpInputStream(imageUrl);
			if (is == null) {
				continue;
			}
			FileOutputStream fos = new FileOutputStream(imageFile);
			IoUtil.io(is, fos);
			++count;
		}
		
		return count;
	}
	
	private static InputStream getHttpInputStream(String imageUrl)  {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			return conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
