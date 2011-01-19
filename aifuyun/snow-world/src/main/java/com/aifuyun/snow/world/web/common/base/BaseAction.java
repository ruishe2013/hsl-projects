package com.aifuyun.snow.world.web.common.base;

import com.zjuh.splist.core.form.token.TokenUtil;
import com.zjuh.splist.web.TemplateContext;

/**
 * @author ally
 *
 */
public abstract class BaseAction extends BaseModule {

	protected boolean checkCsrf(TemplateContext templateContext) {
		if (!TokenUtil.checkToken()) {
			templateContext.put("csrfmessage", "请不要重复提交！");
			return false;
		}
		return true;
	}
	
	protected boolean checkCsrf(TemplateContext templateContext, String name) {
		if (!TokenUtil.checkToken(name)) {
			templateContext.put("csrfmessage", "请不要重复提交！");
			return false;
		}
		return true;
	}
	
}
