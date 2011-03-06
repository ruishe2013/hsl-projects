package com.aifuyun.snow.world.web.modules.screen;

import com.aifuyun.snow.world.biz.ao.misc.SnowWorldAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class Index extends BaseScreen {

	private SnowWorldAO snowWorldAO;
	
	private int cityCount = 10;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = snowWorldAO.handleForIndex(cityCount);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "recentOrders");
			this.result2Context(result, templateContext, "selectedCity");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setSnowWorldAO(SnowWorldAO snowWorldAO) {
		this.snowWorldAO = snowWorldAO;
	}


}
