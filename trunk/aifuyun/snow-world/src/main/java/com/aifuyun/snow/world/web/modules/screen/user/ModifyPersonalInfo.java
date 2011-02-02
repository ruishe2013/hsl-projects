package com.aifuyun.snow.world.web.modules.screen.user;

import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class ModifyPersonalInfo extends BaseScreen {

	private UserAO userAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = userAO.viewModifyPersonalInfo();
		if (result.isSuccess()) {
			final Form form = rundata.getForm("user.personalInfo");
			BaseUserDO user = (BaseUserDO)result.getModels().get("user");
			if (!form.isHeldValues()) {
				form.holdValues(user);
			}
			templateContext.put("user", user);
			this.result2Context(result, templateContext, "years");
			this.result2Context(result, templateContext, "selectedYear");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

}
