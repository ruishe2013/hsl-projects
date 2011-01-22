package com.aifuyun.snow.world.web.modules.control.common;

import com.aifuyun.snow.world.web.common.base.BaseControl;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class CheckCode extends BaseControl {

	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		templateContext.put("validatorUrl", getValidatorUrl());
	}

}
