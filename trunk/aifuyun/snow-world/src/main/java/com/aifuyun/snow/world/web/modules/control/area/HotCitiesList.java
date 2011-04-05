package com.aifuyun.snow.world.web.modules.control.area;

import com.aifuyun.snow.world.biz.ao.area.CityAO;
import com.aifuyun.snow.world.web.common.base.BaseControl;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class HotCitiesList extends BaseControl {

	private CityAO cityAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = cityAO.queryHotCityList();
		if (result.isSuccess()) {
			this.result2Context(result, templateContext);
		}
	}

	public void setCityAO(CityAO cityAO) {
		this.cityAO = cityAO;
	}

}
