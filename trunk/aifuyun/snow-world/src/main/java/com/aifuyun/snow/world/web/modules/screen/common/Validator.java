package com.aifuyun.snow.world.web.modules.screen.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.bo.captcha.ValidateService;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class Validator extends BaseScreen {

	private static final Logger log = LoggerFactory.getLogger(Validator.class);
	
	private ValidateService validateService;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		try {
			rundata.setUseTemplate(false);
	        String sessionId = rundata.getQueryString().getString("sessionId");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			validateService.generate(sessionId, bos);
			
			HttpServletResponse response = SplistContext.getResponse();
			response.setHeader("Pragma", "no-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires",0);
			OutputStream os = response.getOutputStream();
			os.write(bos.toByteArray());
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("生成验证码出错", e);
		}
	}

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

}
