package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class CreateTaxiOrder extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		Result result = orderAO.viewCreateTaxiOrder();
		if (result.isSuccess()) {
			final Form form = rundata.getForm("together.createTaxiOrder");
			OrderDO order = (OrderDO)result.getModels().get("order");
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
