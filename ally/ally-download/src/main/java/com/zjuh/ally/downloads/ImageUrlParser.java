package com.zjuh.ally.downloads;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageUrlParser {
	
	public static List<String> imageUrl(String urlString) throws MalformedURLException, IOException {
		Document doc = Jsoup.parse(new URL(urlString), 5000);
		Elements elements =  doc.getElementsByTag("img");
		List<String> ret = new ArrayList<String>();
		for (Element el : elements) {
			String src = el.attr("src");
			ret.add(src);
		}		
		return ret;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println(ImageUrlParser.imageUrl("http://tajj.tmall.com/shop/viewShop.htm?prc=1"));
	}

}
