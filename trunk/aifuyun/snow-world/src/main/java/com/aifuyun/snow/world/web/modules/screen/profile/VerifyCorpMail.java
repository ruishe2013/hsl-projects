package com.aifuyun.snow.world.web.modules.screen.profile;

import com.aifuyun.snow.world.biz.ao.user.OnwerCorpMailParam;
import com.aifuyun.snow.world.biz.ao.user.ProfileAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class VerifyCorpMail extends BaseScreen {

	private ProfileAO profileAO;
	
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
		
		rundata.setTarget("profile/verifyCorpMailSuccess.vm");
		
		Result result = profileAO.handleCorpVerifyMail(onwerCorpMailParam);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "profile/verifyCorpMailSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setProfileAO(ProfileAO profileAO) {
		this.profileAO = profileAO;
	}

}
