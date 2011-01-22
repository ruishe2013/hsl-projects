package com.aifuyun.snow.world.web.modules.screen;

import com.aifuyun.snow.world.biz.bo.captcha.CheckCodeService;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class Index extends BaseScreen {

	private CheckCodeService checkCodeService;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String checkcode = rundata.getQueryString().getString("checkcode");
		System.out.println(checkcode);
		
		boolean result = checkCodeService.check(checkcode);
		
		templateContext.put("result", result);
	}

	public void setCheckCodeService(CheckCodeService checkCodeService) {
		this.checkCodeService = checkCodeService;
	}

}
