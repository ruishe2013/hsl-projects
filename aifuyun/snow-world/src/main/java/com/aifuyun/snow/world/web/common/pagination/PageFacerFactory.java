package com.aifuyun.snow.world.web.common.pagination;

import java.util.HashMap;
import java.util.Map;

public class PageFacerFactory {
	
	private static Map<String, PageFacer> pageFacers = new HashMap<String, PageFacer>();
	
	static {
		pageFacers.put("mockPageFacer", new MockPageFacer());
		pageFacers.put("myTogethersPageFacer", new  MyTogethersPageFacer());
		pageFacers.put("orderSearchPageFacer", new  OrderSearchPageFacer());
	}
	
	public static PageFacer getPageFacer(String name) {
		return pageFacers.get(name);
	}

}
