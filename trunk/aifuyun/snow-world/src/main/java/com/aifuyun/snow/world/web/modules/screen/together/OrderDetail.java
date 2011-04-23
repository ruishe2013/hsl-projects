package com.aifuyun.snow.world.web.modules.screen.together;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.StringUtil;
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
			// �� 1,2 �滻�� ����һ ���ڶ�
			templateContext.put("fromWeekFormat", showFromWeekFormat(order));
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private String showFromWeekFormat(OrderDO order) {
		String fromWeek = order.getFromWeek();
		if(fromWeek == null) {
			return null;
		}
		fromWeek = numberToChina(fromWeek);
		return "����"+StringUtil.replaceAll(fromWeek, ',', "����");
	}
	
	
	

	private String numberToChina(String fromWeek) {
		fromWeek = StringUtil.replaceAll(fromWeek, '1', "һ  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '2', "��  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '3', "��  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '4', "��  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '5', "��  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '6', "��  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '7', "��  ");
		return fromWeek;
	}

	protected String buildTitle(OrderDO order) {
		return "�� "+ order.getFromCity() + order.getFromAddr() +" �� "+ order.getArriveCity() + order.getArriveAddr() + " ��ƴ��";
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
