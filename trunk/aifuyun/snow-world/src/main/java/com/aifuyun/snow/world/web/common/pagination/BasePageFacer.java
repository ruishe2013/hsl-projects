package com.aifuyun.snow.world.web.common.pagination;

import com.zjuh.splist.web.TemplateContext;


public abstract class BasePageFacer implements PageFacer {

	protected TemplateContext getTemplateContext() {
		return PageFacerFactory.getTemplateContext();
	}

}
