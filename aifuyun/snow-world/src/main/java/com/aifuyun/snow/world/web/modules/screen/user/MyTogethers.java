package com.aifuyun.snow.world.web.modules.screen.user;

import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class MyTogethers extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int type = rundata.getQueryString().getInt("type", OrderUserRoleEnum.CREATOR.getValue());
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setOrderUserRole(type);
		Result result = orderAO.viewMyOrders(orderQuery);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "orders");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
