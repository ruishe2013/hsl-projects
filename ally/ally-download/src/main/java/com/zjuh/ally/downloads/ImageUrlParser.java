package com.zjuh.ally.downloads;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageUrlParser {
	
	private static boolean isUrlOK(String urls) {
		try {
			new URL(urls);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	private static boolean isTaobao(String urlString) {
		try {
			URL url = new URL(urlString);
			String host = url.getHost();
			if (host.endsWith("taobao.com")) {
				return true;
			}
			return false;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	private static boolean isTaobaoItem(String urlString) {
		try {
			URL url = new URL(urlString);
			String host = url.getHost();
			if (host.endsWith("item.taobao.com")) {
				return true;
			}
			return false;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public static void parsePage(String urlString, PageInfo pageInfo) throws IOException {
		try {
			if (!isUrlOK(urlString)) {
				return;
			}
			if (pageInfo.getLinkUrls().contains(urlString)) {
				return;
			}
			if (!isTaobao(urlString)) {
				return;
			}

			Document doc = Jsoup.parse(new URL(urlString), 5000);
			Set<String> imgUrls = pageInfo.getImgUrls();
			Elements elements =  doc.getElementsByTag("img");
			for (Element el : elements) {
				String bigpicurl = el.attr("bigpicurl");
				if (bigpicurl == null || bigpicurl.length() == 0) {
					String src = el.attr("src");
					imgUrls.add(src);
				} else {
					imgUrls.add(bigpicurl);
				}
			}		
			
			pageInfo.getLinkUrls().add(urlString);
			
			Elements linkElements =  doc.getElementsByTag("a");
			for (Element el : linkElements) {
				String link = el.attr("href");
				if (!isTaobaoItem(link)) {
					continue;
				}
				parsePage(link, pageInfo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> parsePage(String urlString) throws IOException {
		PageInfo p = new PageInfo();
		parsePage(urlString, p);
		return p.getImgUrls();
	}
	
	public static void main(String[] args) throws IOException {
		for (String s : parsePage("http://jiang-hong-lie.taobao.com/?asker=wangwang&wwdialog=tbdasale")) {
			System.out.println(s);
		}
	}

}
