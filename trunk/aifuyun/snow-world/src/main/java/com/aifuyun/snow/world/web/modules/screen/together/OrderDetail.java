package com.aifuyun.snow.world.web.modules.screen.together;

import java.util.HashMap;
import java.util.Map;

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
			
			// #set($title="从 $!{order.fromCity}$!{order.fromAddr} 到 $!{order.arriveCity}$!{order.arriveAddr} 的拼车")
			OrderDO order = (OrderDO)result.getModels().get("order");
			templateContext.put("title", buildTitle(order));
			// 把 1,2 替换成 星期一 星期二
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
		return "星期"+StringUtil.replaceAll(fromWeek, ',', "星期");
	}
	
	
	

	private String numberToChina(String fromWeek) {
		fromWeek = StringUtil.replaceAll(fromWeek, '1', "一  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '2', "二  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '3', "三  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '4', "四  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '5', "五  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '6', "六  ");
		fromWeek = StringUtil.replaceAll(fromWeek, '7', "日  ");
		return fromWeek;
	}

	protected String buildTitle(OrderDO order) {
		return "从 "+ order.getFromCity() + order.getFromAddr() +" 到 "+ order.getArriveCity() + order.getArriveAddr() + " 的拼车";
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
