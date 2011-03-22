package com.aifuyun.snow.world.web.modules.control.common;

import com.aifuyun.snow.world.web.common.base.BaseControl;
import com.aifuyun.snow.world.web.common.pagination.PageFacer;
import com.aifuyun.snow.world.web.common.pagination.PageFacerFactory;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class SearchPagination extends BaseControl {

	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String name = (String)templateContext.get("pageFacerName");
		PageFacer pageFacer = PageFacerFactory.getPageFacer(name);
		templateContext.put("pageChangerFaced", pageFacer);
	}

}
