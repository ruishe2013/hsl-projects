package com.aifuyun.snow.world.web.modules.action.together;

import java.util.Date;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public class OrderAction extends BaseAction {
	
	private static final String DATE_FMT = "yyyy-MM-dd";

	private OrderAO orderAO;
	
	@DefaultTarget("together/orderDetail")
	public void doCancelOrder(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		Result result = orderAO.cancelOrder(orderId);
		if (result.isSuccess()) {
			redirect2CancelOrderSuccessPage();
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private void redirect2CancelOrderSuccessPage() {
		this.sendRedirect("snowModule", "together/cancelOrderSuccess.vm");
	}
	
	@DefaultTarget("together/orderDetail")
	public void doConfirmTogetherOrder(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		Result result = orderAO.confirmTogetherOrder(orderId);
		if (result.isSuccess()) {
			sendToOrderDetailPage(orderId);
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/orderDetail")
	public void doExitOrder(RunData rundata, TemplateContext templateContext) {
		long id = rundata.getQueryString().getLong("id");
		Result result = orderAO.exitOrder(id);
		if (result.isSuccess()) {
			OrderDO order = (OrderDO)result.getModels().get("order");
			sendToOrderDetailPage(order.getId());
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/orderDetail")
	public void doRemoveUserFromOrder(RunData rundata, TemplateContext templateContext) {
		long id = rundata.getQueryString().getLong("id");
		Result result = orderAO.removeUserFromOrder(id);
		if (result.isSuccess()) {
			OrderDO order = (OrderDO)result.getModels().get("order");
			sendToOrderDetailPage(order.getId());
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/orderDetail")
	public void doConfirmUserJoin(RunData rundata, TemplateContext templateContext) {
		long id = rundata.getQueryString().getLong("id");
		boolean agree = rundata.getQueryString().getBoolean("agree");
		Result result = orderAO.confirmUserJoin(id, agree);
		if (result.isSuccess()) {
			OrderDO order = (OrderDO)result.getModels().get("order");
			sendToOrderDetailPage(order.getId());
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/orderDetail")
	public void doJoinOrder(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("together.personalInfo");
		if (!form.validate()) {
			return;
		}
		OrderUserDO orderUserDO = new OrderUserDO();
		form.apply(orderUserDO);
		
		long orderId = rundata.getQueryString().getLong("orderId");
		boolean saveToUserInfo = rundata.getQueryString().getBoolean("saveToUserInfo");
		
		Result result = orderAO.joinOrder(orderUserDO, orderId, saveToUserInfo);
		if (result.isSuccess()) {
			// 加入成功
			this.sendRedirect("snowModule", "together/joinSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/confirmOrder")
	public void doConfirmOrder(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		Result result = orderAO.confirmFinishOrder(orderId);
		if (result.isSuccess()) {
			this.sendRedirect("snowModule", "together/createOrderSuccess");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	@DefaultTarget("together/personalInfo")
	public void doFillCreatorInfo(RunData rundata, TemplateContext templateContext) {
		final Form form = rundata.getForm("together.personalInfo");
		if (!form.validate()) {
			return;
		}
		OrderUserDO orderUserDO = new OrderUserDO();
		form.apply(orderUserDO);
		
		long orderId = rundata.getQueryString().getLong("orderId");
		boolean saveToUserInfo = rundata.getQueryString().getBoolean("saveToUserInfo");
		Result result = orderAO.fillCreatorInfo(orderUserDO, orderId, saveToUserInfo);
		if (result.isSuccess()) {
			sendToConfirmPage(orderId);
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private void sendToConfirmPage(long orderId) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
		URLModule urlModule = urlModuleContainer.setTarget("together/confirmOrder");
		urlModule.add("orderId", orderId);
		sendRedirectUrl(urlModule.render());
	}
	
	private void sendToOrderDetailPage(long orderId) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
		URLModule urlModule = urlModuleContainer.setTarget("together/orderDetail");
		urlModule.add("orderId", orderId);
		sendRedirectUrl(urlModule.render());
	}
	
	@DefaultTarget("together/createOrder")
	public void doCreateOrder(RunData rundata, TemplateContext templateContext) {
		if (!checkCsrf(templateContext, "createOrder")) {
			return;
		}
		final Form form = rundata.getForm("together.createOrder");
		if (!form.validate()) {
			return;
		}
		OrderDO orderDO = new OrderDO();
		form.apply(orderDO);
		
		Date fromTimeDate = rundata.getQueryString().getDate("fromTimeDate", DATE_FMT);
		
		int fromHour =  rundata.getQueryString().getInt("fromTimeHour");
		int fromMinute =  rundata.getQueryString().getInt("fromTimeMinute");
		
		int arriveHour =  rundata.getQueryString().getInt("arriveTimeHour", -1);
		int arriveMinute =  rundata.getQueryString().getInt("arriveTimeMinute", 0);
		
		Date fromTime = DateTimeUtil.componentDateAndTime(fromTimeDate, fromHour, fromMinute, 0);
		
		Date arriveTime = null;
		if (arriveHour != -1) {
			Date arriveTimeDate = fromTimeDate;
			if (arriveHour < fromHour) {
				// 如果到达时间比出发时间迟，默认作为第二天处理
				arriveTimeDate = DateUtil.addDay(fromTimeDate, 1);
			}
			arriveTime = DateTimeUtil.componentDateAndTime(arriveTimeDate, arriveHour, arriveMinute, 0);
		}
		
		orderDO.setFromTime(fromTime);
		orderDO.setArriveTime(arriveTime);
		
		// 目的城市和出发城市是同一个
		orderDO.setArriveCityId(orderDO.getCityId());
		orderDO.setArriveCity(orderDO.getFromCity());
		
		Result result = orderAO.createOrder(orderDO);
		if (result.isSuccess()) {
			long orderId = (Long)result.getModels().get("orderId");
			sendToPersonalInfoPage(orderId);			
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}
	
	private void sendToPersonalInfoPage(long orderId) {
		URLModuleContainer urlModuleContainer = getURLModuleContainer("snowModule");
		URLModule urlModule = urlModuleContainer.setTarget("together/personalInfo");
		urlModule.add("orderId", orderId);
		sendRedirectUrl(urlModule.render());
	}
	
	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
