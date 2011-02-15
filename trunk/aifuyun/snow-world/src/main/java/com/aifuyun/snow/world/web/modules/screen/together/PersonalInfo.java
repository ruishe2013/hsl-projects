package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class PersonalInfo extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		boolean join = rundata.getQueryString().getBoolean("join");
		Result result = orderAO.viewPersonalInfoForOrder(orderId, join);
		if (!result.isSuccess()) {
			this.handleError(result, rundata, templateContext);
			return;
		}
		
		final Form form = rundata.getForm("together.personalInfo");
		OrderUserDO orderUser = (OrderUserDO)result.getModels().get("orderUser");
		if (!form.isHeldValues()) {
			form.holdValues(orderUser);
		}
		
		this.result2Context(result, templateContext, "actionEvent");
		this.result2Context(result, templateContext, "creatorExist");
		this.result2Context(result, templateContext, "isUserInfoEmpty");
		this.result2Context(result, templateContext, "selectedYear");
		this.result2Context(result, templateContext, "years");
		this.result2Context(result, templateContext, "orderUser");
		this.result2Context(result, templateContext, "order");
		
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
