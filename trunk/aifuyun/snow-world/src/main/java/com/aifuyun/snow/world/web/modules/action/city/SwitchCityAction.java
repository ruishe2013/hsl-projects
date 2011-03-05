package com.aifuyun.snow.world.web.modules.action.city;

import com.aifuyun.snow.world.biz.ao.area.CityAO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class SwitchCityAction extends BaseAction {

	private CityAO cityAO;
	
	@DefaultTarget("city/cityList")
	public void doSwitchCity(RunData rundata, TemplateContext templateContext) {
		int cityId = rundata.getQueryString().getInt("cityId");
		Result result = cityAO.switchCity(cityId);
		if (result.isSuccess()) {
			sendToIndexPage();
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setCityAO(CityAO cityAO) {
		this.cityAO = cityAO;
	}
	
}
