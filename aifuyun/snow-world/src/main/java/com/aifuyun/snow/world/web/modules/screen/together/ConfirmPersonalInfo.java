package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class ConfirmPersonalInfo extends BaseScreen {

	private UserAO userAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = userAO.confirmPersonalInfo();
		if (!result.isSuccess()) {
			this.handleError(result, rundata, templateContext);
			return;
		}
		boolean userInfoCompeleted = (Boolean)result.getModels().get("userInfoCompeleted");
		if (!userInfoCompeleted) {
			// ����������ϲ������� ��ת��������ɸ������Ͻ���
			this.sendRedirect("snowModule", "user/modifyPersonalInfo");
			return;
		}
		this.result2Context(result, templateContext, "user");
		this.result2Context(result, templateContext, "userInfoCompeleted");
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

}
