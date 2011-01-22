package com.aifuyun.snow.world.web.modules.action;

import com.aifuyun.snow.world.biz.ao.user.LoginAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.author.AuthorConstants;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;

public class LoginAction extends BaseAction {

	private LoginAO loginAO;
	
	@DefaultTarget("login.vm")
	public void doLogin(RunData rundata, TemplateContext templateContext) {
		if (!checkCsrf(templateContext, "login")) {
			return;
		}
		final Form form = rundata.getForm("login");
		if (!form.validate()) {
			return;
		}
		form.holdValues();
		BaseUserDO inputUser = new BaseUserDO();
		form.apply(inputUser);
		
		Result result = loginAO.handleLogin(inputUser);
		if (result.isSuccess()) {
			String url = rundata.getQueryString().getString(AuthorConstants.REDIRECT_URL_NAME);
			if (StringUtil.isEmpty(url)) {
				URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
				URLModule urlModule = urlModuleContainer.setTarget("index");
				rundata.sendRedirect(urlModule.render());
			} else {
				rundata.sendRedirect(url);
			}
		} else {
			this.handleError(result, rundata, templateContext);
		}
		
	}
	
	public void setLoginAO(LoginAO loginAO) {
		this.loginAO = loginAO;
	}
	
}
