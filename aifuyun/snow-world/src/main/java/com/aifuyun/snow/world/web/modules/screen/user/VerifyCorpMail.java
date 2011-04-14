package com.aifuyun.snow.world.web.modules.screen.user;

import com.aifuyun.snow.world.biz.ao.user.OnwerCorpMailParam;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class VerifyCorpMail extends BaseScreen {

	private UserAO userAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		long ts = rundata.getQueryString().getLong("ts");
		String email = rundata.getQueryString().getString("email");
		String token = rundata.getQueryString().getString("token");
		long userId = rundata.getQueryString().getLong("userId");
		
		OnwerCorpMailParam onwerCorpMailParam = new OnwerCorpMailParam();
		onwerCorpMailParam.setTimestamp(ts);
		onwerCorpMailParam.setEmail(email);
		onwerCorpMailParam.setToken(token);
		onwerCorpMailParam.setUserId(userId);
		
		Result result = userAO.handleCorpVerifyMail(onwerCorpMailParam);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "user/verifyCorpMailSuccess");
			//this.result2Context(result, templateContext);
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

}
