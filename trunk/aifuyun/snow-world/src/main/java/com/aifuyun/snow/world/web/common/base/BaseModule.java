package com.aifuyun.snow.world.web.common.base;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
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
import com.zjuh.sweet.result.ResultCodeTypeEnum;


/**
 * @author ally
 *
 */
public class BaseModule {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * ��ȡ��ǰ��  url
	 * @return
	 */
	protected String getCurrentUrl() {
		HttpServletRequest request = SplistContext.getRequest();
		return UrlUtil.getRequestURL(request);
	}
	
	/**
	 * ��ȡ��֤�����ӵ�ַ
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
	 * ��ȡ��½��url
	 * @param withCurrentUrl
	 * @return
	 */
	protected String getLoginUrl(boolean withCurrentUrl) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
		URLModule urlModule = urlModuleContainer.setTarget("login.vm");
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
	 * ��result�еĶ�����뵽context
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
	 * ��ȡ���õ�url
	 * @param rundata
	 * @return
	 */
	protected String getBackUrl(RunData rundata) {
		HttpServletRequest request = SplistContext.getRequest();
		return request.getHeader("referer");
	}
	
	/**
	 * �������
	 * @param result
	 * @param rundata
	 * @param templateContext
	 */
	protected void handleError(Result result, RunData rundata, TemplateContext templateContext) {
		ResultCodeTypeEnum resultCodeTypeEnum = result.getResultCode().getResultCodeTypeEnum();
		ResultCode resultCode = result.getResultCode();
		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
		if (ResultCodeTypeEnum.COMMON_TARGET.equals(resultCodeTypeEnum)) {
			templateContext.put("errorMessage", resultCode);
			templateContext.put("backUrl", getBackUrl(rundata));
			rundata.setTarget("error.vm");
		} else if(ResultCodeTypeEnum.CURRENT_TARGET.equals(resultCodeTypeEnum)) {
			templateContext.put("resultmessage", resultCode);
		}
		
	}
	

}
