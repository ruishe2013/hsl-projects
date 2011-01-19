package com.aifuyun.snow.world.web.modules.screen;

import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class Index extends BaseScreen {

	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String name = rundata.getQueryString().getString("name");
		System.out.println(name);
		
		templateContext.put("name", name);
	}

}
