package com.aifuyun.snow.world.web.common.pagination;

import com.aifuyun.snow.world.biz.query.UserOrderQuery;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.TemplateContext;

public class MyTogethersPageFacer implements PageFacer {

	@Override
	public URLModule gotoPage(int page) {
		URLModuleContainer container = SplistContext.getSplistComponent().getUrlModuleContainers().get("snowModule");
		URLModule urlModule = container.setTarget("profile/myTogethers");
		TemplateContext templateContext = SplistContext.getTemplateContext();
		UserOrderQuery query = (UserOrderQuery)templateContext.get("query");
		urlModule.addQueryData("page", page);
		urlModule.addQueryData("type", query.getRole());
		return urlModule;
	}

}
