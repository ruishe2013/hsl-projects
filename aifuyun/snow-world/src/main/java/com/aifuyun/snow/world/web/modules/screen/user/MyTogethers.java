package com.aifuyun.snow.world.web.modules.screen.user;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.biz.query.UserOrderQuery;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class MyTogethers extends BaseScreen {

	private OrderAO orderAO;
	
	private int pageSize = 20;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int type = rundata.getQueryString().getInt("type");
		int page = rundata.getQueryString().getInt("page", 1);
		UserOrderQuery userOrderQuery = new UserOrderQuery();
		userOrderQuery.setPageNo(page);
		userOrderQuery.setPageSize(pageSize);
		userOrderQuery.setRole(type);
		Result result = orderAO.viewMyOrders(userOrderQuery);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "orders");
			this.result2Context(result, templateContext, "userOrderQuery");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
