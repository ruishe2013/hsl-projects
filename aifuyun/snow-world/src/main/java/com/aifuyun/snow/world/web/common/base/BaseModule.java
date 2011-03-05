package com.aifuyun.snow.world.web.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.common.IpUtil;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.splist.web.uri.BaseURI;
import com.zjuh.splist.web.uri.UrlUtil;
import com.zjuh.sweet.author.AuthorConstants;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultCode;
import com.zjuh.sweet.result.ResultTypeEnum;


/**
 * @author ally
 *
 */
public class BaseModule {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected String getRemoteAddress() {
		return IpUtil.getRemoteIpAddress();
	}
	
	/**
	 * 获取当前的  url
	 * @return
	 */
	protected String getCurrentUrl() {
		HttpServletRequest request = SplistContext.getRequest();
		return UrlUtil.getRequestURL(request);
	}
	
	/**
	 * 获取验证码链接地址
	 * @return
	 */
	protected String getValidatorUrl() {
		BaseURI baseURI = SplistContext.getSplistComponent().getConfigUries().get("validatorServer");
		StringBuilder sb = new StringBuilder();
		sb.append(baseURI.render());
		sb.append("?sessionId=");
		sb.append(SplistContext.getSession().getId());
		return sb.toString();
	}
	
	/**
	 * 获取登陆的url
	 * @param withCurrentUrl
	 * @return
	 */
	protected String getLoginUrl(boolean withCurrentUrl) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
		URLModule urlModule = urlModuleContainer.setTarget("login");
		if (withCurrentUrl) {
			urlModule.addQueryData(AuthorConstants.REDIRECT_URL_NAME, getCurrentUrl());
		}
		return urlModule.render();
	}
	
	/**
	 * url module
	 * @param module
	 * @return
	 */
	protected URLModuleContainer getURLModuleContainer(String module) {
		URLModuleContainer urlModuleContainer = SplistContext.getSplistComponent().getUrlModuleContainers().get(module);
		return urlModuleContainer;
	}
	
	/**
	 * 把result中的对象放入到context
	 * @param result
	 * @param templateContext
	 * @param name
	 * @return
	 */
	protected Object result2Context(Result result, TemplateContext templateContext, String name) {
		Object value = result.getModels().get(name);
		templateContext.put(name, value);
		return value;
	}
	
	/**
	 * 获取引用的url
	 * @param rundata
	 * @return
	 */
	protected String getBackUrl(RunData rundata) {
		HttpServletRequest request = SplistContext.getRequest();
		return request.getHeader("referer");
	}
	
	/**
	 * 处理出错
	 * @param result
	 * @param rundata
	 * @param templateContext
	 */
	protected void handleError(Result result, RunData rundata, TemplateContext templateContext) {
		ResultTypeEnum resultCodeTypeEnum = result.getResultTypeEnum();
		ResultCode resultCode = result.getResultCode();
		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
		if (ResultTypeEnum.COMMON_TARGET.equals(resultCodeTypeEnum)) {
			templateContext.put("errorMessage", resultCode);
			templateContext.put("backUrl", getBackUrl(rundata));
			rundata.setRedirectTarget("error");
		} else if(ResultTypeEnum.CURRENT_TARGET.equals(resultCodeTypeEnum)) {
			templateContext.put("resultmessage", resultCode);
		}
		
	}
	
	protected void sendToIndexPage() {
		sendRedirect("snowModule", "index");
	}
	
	protected void sendRedirectUrl(String url) {
		try {
			SplistContext.getResponse().sendRedirect(url);
		} catch (IOException e) {
			log.error("重定向失败", e);
		}
	}

	protected void sendRedirect(String module, String target) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer(module);
		URLModule urlModule = urlModuleContainer.setTarget(target);
		sendRedirectUrl(urlModule.render());
	}
	
}
