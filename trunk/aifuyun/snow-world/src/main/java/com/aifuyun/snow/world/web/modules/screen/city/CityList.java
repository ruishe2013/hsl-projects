package com.aifuyun.snow.world.web.modules.screen.city;

import com.aifuyun.snow.world.biz.ao.area.CityAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class CityList extends BaseScreen {

	private CityAO cityAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = cityAO.queryAllProviceAndCities();
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "province2Cities");
		}
	}

	public void setCityAO(CityAO cityAO) {
		this.cityAO = cityAO;
	}

}
