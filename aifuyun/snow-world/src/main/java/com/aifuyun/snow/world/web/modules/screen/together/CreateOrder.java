package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;

public class CreateOrder extends BaseScreen {

	private OrderAO orderAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int orderType = rundata.getQueryString().getInt("orderType",1);
		long orderId = rundata.getQueryString().getLong("orderId", 0L);
		Result result = orderAO.viewCreateOrder(orderId, orderType);
		if (result.isSuccess()) {
			final Form form = rundata.getForm("together.createOrder");
			OrderDO order = (OrderDO)result.getModels().get("order");
			
			this.result2Context(result, templateContext);
			templateContext.put("orderType", orderType);
			
			if (!form.isHeldValues()) {
				form.holdValues(order);
				if(order != null) {
					setFieldValue(form.getFields().get("fromTimeDate"), DateUtil.formatDateYMD(order.getFromTime()));
					
					setFieldValue(form.getFields().get("fromTimeHour"), trimZero(DateUtil.formatDate(order.getFromTime(), "HH")));
					setFieldValue(form.getFields().get("fromTimeMinute"), trimZero(DateUtil.formatDate(order.getFromTime(), "mm")));
					
					setFieldValue(form.getFields().get("arriveTimeHour"), trimZero(DateUtil.formatDate(order.getArriveTime(), "HH")));
					setFieldValue(form.getFields().get("arriveTimeMinute"),  trimZero(DateUtil.formatDate(order.getArriveTime(), "mm")));
				}
			}
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
