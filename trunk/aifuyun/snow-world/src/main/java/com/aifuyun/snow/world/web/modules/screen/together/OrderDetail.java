package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
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
			
			// #set($title="�� $!{order.fromCity}$!{order.fromAddr} �� $!{order.arriveCity}$!{order.arriveAddr} ��ƴ��")
			OrderDO order = (OrderDO)result.getModels().get("order");
			templateContext.put("title", buildTitle(order));
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	protected String buildTitle(OrderDO order) {
		return "�� "+ order.getFromCity() + order.getFromAddr() +" �� "+ order.getArriveCity() + order.getArriveAddr() + " ��ƴ��";
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
