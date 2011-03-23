package com.zjuh.ally.downloads;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageUrlParser {
	
	public static Set<String> imageUrl(String urlString) throws MalformedURLException, IOException {
		Document doc = Jsoup.parse(new URL(urlString), 5000);
		Elements elements =  doc.getElementsByTag("img");
		Set<String> ret = new HashSet<String>();
		for (Element el : elements) {
			String bigpicurl = el.attr("bigpicurl");
			if (bigpicurl == null || bigpicurl.length() == 0) {
				String src = el.attr("src");
				ret.add(src);
			} else {
				ret.add(bigpicurl);
			}
			
		}		
		return ret;
	}

}
