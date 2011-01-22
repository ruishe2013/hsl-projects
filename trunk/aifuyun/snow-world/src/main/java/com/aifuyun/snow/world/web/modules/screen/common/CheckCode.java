package com.aifuyun.snow.world.web.modules.screen.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.aifuyun.snow.world.biz.bo.captcha.CheckCodeService;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public class CheckCode extends BaseScreen {

	private CheckCodeService checkCodeService;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		try {
			rundata.setUseTemplate(false);
	        String sessionId = rundata.getQueryString().getString("sessionId");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			checkCodeService.generateNext(sessionId, bos);
			
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

	public void setCheckCodeService(CheckCodeService checkCodeService) {
		this.checkCodeService = checkCodeService;
	}

}
