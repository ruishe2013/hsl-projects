package com.aifuyun.snow.world.web.modules.action;

import com.aifuyun.snow.world.biz.ao.user.LoginAO;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class UserFacadeAction extends BaseAction {

	private UserAO userAO;
	
	private LoginAO loginAO;
	
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
		String username = baseUserDO.getUsername();
		String password = baseUserDO.getPassword();
		Result result = userAO.registerUser(baseUserDO);
		if (result.isSuccess()) {
			this.handleLogin(username, password);
			this.sendRedirect("snowModule", "user/regSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private void handleLogin(String username, String password) {
		BaseUserDO user = new BaseUserDO();
		user.setUsername(username);
		user.setPassword(password);
		loginAO.handleLogin(user);
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

	public void setLoginAO(LoginAO loginAO) {
		this.loginAO = loginAO;
	}
	
}
