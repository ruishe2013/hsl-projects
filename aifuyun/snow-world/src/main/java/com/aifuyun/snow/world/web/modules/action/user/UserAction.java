package com.aifuyun.snow.world.web.modules.action.user;

import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class UserAction extends BaseAction {
	
	private UserAO userAO;
	
	@DefaultTarget("user/modifyPersonalInfo")
	public void doModifyPersonalInfo(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("user.personalInfo");
		if (!form.validate()) {
			return;
		}
		BaseUserDO inputBaseUser = new BaseUserDO();
		form.apply(inputBaseUser);
		
		Result result = userAO.modifyPersonalInfo(inputBaseUser);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "together/confirmPersonalInfo");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

}
