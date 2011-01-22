package com.aifuyun.snow.world.web.modules.action;

import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class UserFacadeAction extends BaseAction {

	private UserAO userAO;
	
	@DefaultTarget("user/reg")
	public void doRegUser(RunData rundata, TemplateContext templateContext) {
		if (!checkCsrf(templateContext, "regUser")) {
			return;
		}
		final Form form = rundata.getForm("user.registerUser");
		if (!form.validate()) {
			return;
		}
		
		BaseUserDO baseUserDO = new BaseUserDO();
		form.apply(baseUserDO);
		Result result = userAO.registerUser(baseUserDO);
		if (result.isSuccess()) {
			sendRedirectUrl(getRegSuccessUrl());
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	private String getRegSuccessUrl() {
		URLModuleContainer c = this.getURLModuleContainer("snowModule");
		URLModule url = c.setTarget("user/regSuccess");
		return url.render();
	}
	
	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}
	
}
