package com.aifuyun.snow.world.web.modules.action.profile;

import com.aifuyun.snow.world.biz.ao.user.ProfileAO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;

public class UserProfileAction extends BaseAction {
	
	private ProfileAO profileAO;
	
	@DefaultTarget("profile/viewVerify")
	public void doSendCorpVerifyMail(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("user.sendCorpVerifyMail");
		if (!form.validate()) {
			return;
		}
		String email = StringUtil.toString(form.getFields().get("email").getValue());
		Result result = profileAO.sendCorpVerifyMail(email);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "profile/sendMailSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	@DefaultTarget("profile/changePwd")
	public void doChangePwd(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("user.changePwd");
		if (!form.validate()) {
			return;
		}
		String oldPassword = StringUtil.toString(form.getFields().get("oldPassword").getValue());
		String password = StringUtil.toString(form.getFields().get("password").getValue());
		
		Result result = profileAO.changePassword(oldPassword, password);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "profile/changePwdSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	public void setProfileAO(ProfileAO profileAO) {
		this.profileAO = profileAO;
	}
	
}
