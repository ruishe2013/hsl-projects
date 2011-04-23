package com.aifuyun.snow.world.web.common.pagination;

import java.util.HashMap;
import java.util.Map;

import com.zjuh.splist.web.TemplateContext;

public class PageFacerFactory {
	
	private static Map<String, PageFacer> pageFacers = new HashMap<String, PageFacer>();
	
	private static ThreadLocal<TemplateContext> targetTemplateContext = new ThreadLocal<TemplateContext>();
	
	static {
		pageFacers.put("mockPageFacer", new MockPageFacer());
		pageFacers.put("myTogethersPageFacer", new MyTogethersPageFacer());
		pageFacers.put("orderSearchPageFacer", new OrderSearchPageFacer());
	}
	
	public static void clearTemplateContext() {
		targetTemplateContext.remove();
	}
	
	public static TemplateContext getTemplateContext() {
		return targetTemplateContext.get();
	}
	
	public static PageFacer getPageFacer(String name, TemplateContext templateContext) {
		targetTemplateContext.set(templateContext);
		return pageFacers.get(name);
	}

}
