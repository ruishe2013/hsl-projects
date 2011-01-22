package com.aifuyun.snow.world.web.modules.screen;

import com.aifuyun.snow.world.biz.ao.user.LoginAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

/**
 * @author ally
 *
 */
public class LoginOut extends BaseScreen {

	private LoginAO loginAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		loginAO.handleLogout();
		rundata.sendRedirect(this.getLoginUrl(false));
	}

	public void setLoginAO(LoginAO loginAO) {
		this.loginAO = loginAO;
	}
	

}
