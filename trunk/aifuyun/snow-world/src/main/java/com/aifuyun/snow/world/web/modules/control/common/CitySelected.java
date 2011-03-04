package com.aifuyun.snow.world.web.modules.control.common;

import com.aifuyun.snow.world.biz.ao.area.CityAO;
import com.aifuyun.snow.world.common.CookieNames;
import com.aifuyun.snow.world.web.common.base.BaseControl;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class CitySelected extends BaseControl {

	private CityAO cityAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String ipAddress = this.getRemoteAddress();
		String cityIdFromCookie = this.getCookieValue(CookieNames.LAST_SELECT_CITY_ID);
		Result result = cityAO.querySelectedCity(ipAddress, cityIdFromCookie);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "selectedCity");
		}
	}

	public void setCityAO(CityAO cityAO) {
		this.cityAO = cityAO;
	}

}
