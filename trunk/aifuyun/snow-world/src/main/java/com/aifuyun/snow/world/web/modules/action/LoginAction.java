package com.aifuyun.snow.world.web.modules.action;

import java.net.MalformedURLException;
import java.net.URL;

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
		
		// 把用户名设置到email字段，以便也尝试用email登陆
		inputUser.setEmail(inputUser.getUsername());
		
		Result result = loginAO.handleLogin(inputUser);
		if (result.isSuccess()) {
			String url = rundata.getQueryString().getString(AuthorConstants.REDIRECT_URL_NAME);
			if (!canRedirect(url)) {
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
	
	private boolean canRedirect(String url) {
		// 是否可以跳转到相应页面
		// 如果原来就是登陆页面，则返回false，也就是跳转到首页
		if (StringUtil.isEmpty(url)) {
			return false;
		}
		try {
			URL urlObject = new URL(url);
			String path = urlObject.getPath();
			String target = StringUtil.getLastAfter(path, "/");
			String targetName = StringUtil.getLastBefore(target, ".");
			if ("login".equalsIgnoreCase(targetName)) {
				return false;
			}
			
			// TODO 判断跳转到站外域名
			
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public void setLoginAO(LoginAO loginAO) {
		this.loginAO = loginAO;
	}
	
}
