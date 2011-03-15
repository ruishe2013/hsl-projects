package com.aifuyun.snow.world.web.modules.action;

import com.aifuyun.snow.world.biz.ao.feedback.LeaveWordAO;
import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class FeedbackAction extends BaseAction {

	private LeaveWordAO leaveWordAO;
	
	@DefaultTarget("feedback/leaveWord")
	public void doLeaveWord(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("feedback.leaveWord");
		if (!form.validate()) {
			return;
		}
		form.holdValues();
		LeaveWordDO leaveWord = new LeaveWordDO();
		form.apply(leaveWord);
		leaveWord.setFromIp(this.getRemoteAddress());
		Result result = leaveWordAO.createLeaveWord(leaveWord);
		if (result.isSuccess()) {
			send2LeaveWordSuccessPage();
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private void send2LeaveWordSuccessPage() {
		this.sendRedirect("snowModule", "feedback/leaveWordSuccess");
	}

	public void setLeaveWordAO(LeaveWordAO leaveWordAO) {
		this.leaveWordAO = leaveWordAO;
	}
	
	
}
