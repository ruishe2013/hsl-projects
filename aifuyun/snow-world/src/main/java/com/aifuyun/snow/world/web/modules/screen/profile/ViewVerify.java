package com.aifuyun.snow.world.web.modules.screen.profile;

import com.aifuyun.snow.world.biz.ao.user.ProfileAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class ViewVerify extends BaseScreen {

	private ProfileAO profileAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = profileAO.viewCorpVerifyMailPage();
		if (result.isSuccess()) {
			this.result2Context(result, templateContext);
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setProfileAO(ProfileAO profileAO) {
		this.profileAO = profileAO;
	}

}
