package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class OrderDetail extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		Result result = orderAO.viewOrderDetail(orderId);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext);
			
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
