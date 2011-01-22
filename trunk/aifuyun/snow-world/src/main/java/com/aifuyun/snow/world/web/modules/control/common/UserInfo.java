package com.aifuyun.snow.world.web.modules.control.common;

import com.aifuyun.snow.world.web.common.base.BaseControl;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.lang.StringUtil;

public class UserInfo extends BaseControl {
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String username = LoginContext.getUsername();
		if (StringUtil.isEmpty(username)) {
			templateContext.put("currentURL", getCurrentUrl());
		} else {
			templateContext.put("username", username);
		}
		
	}

}
