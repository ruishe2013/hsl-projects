package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class CreateOrder extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int orderType = rundata.getQueryString().getInt("orderType",1);
		Result result = orderAO.viewCreateOrder(orderType);
		if (result.isSuccess()) {
			final Form form = rundata.getForm("together.createOrder");
			OrderDO order = (OrderDO)result.getModels().get("order");
			
			this.result2Context(result, templateContext);
			templateContext.put("orderType", orderType);
			
			if (!form.isHeldValues()) {
				form.holdValues(order);
			}
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
