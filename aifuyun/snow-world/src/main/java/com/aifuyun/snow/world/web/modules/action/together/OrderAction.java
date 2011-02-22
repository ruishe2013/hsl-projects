package com.aifuyun.snow.world.web.modules.action.together;

import java.util.Date;

import com.aifuyun.snow.world.biz.ao.together.OrderAO;
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
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public class OrderAction extends BaseAction {
	
	private static final String DATE_FMT = "yyyy-MM-dd";

	private static final String TIME_FMT = "HH:mm";
	
	private OrderAO orderAO;
	
	@DefaultTarget("together/orderDetail")
	public void doConfirmUserJoin(RunData rundata, TemplateContext templateContext) {
		long orderId = rundata.getQueryString().getLong("orderId");
		long userId = rundata.getQueryString().getLong("userId");
		boolean agree = rundata.getQueryString().getBoolean("agree");
		Result result = orderAO.confirmUserJoin(orderId, userId, agree);
		if (result.isSuccess()) {
			sendToOrderDetailPage(orderId);
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
		Result result = orderAO.confirmOrder(orderId);
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
	
	@DefaultTarget("together/createTaxiOrder")
	public void doCreateTaxiOrder(RunData rundata, TemplateContext templateContext) {
		if (!checkCsrf(templateContext, "createTaxiOrder")) {
			return;
		}
		final Form form = rundata.getForm("together.createTaxiOrder");
		if (!form.validate()) {
			return;
		}
		OrderDO orderDO = new OrderDO();
		form.apply(orderDO);
		
		Date fromTimeDate = rundata.getQueryString().getDate("fromTimeDate", DATE_FMT);
		Date fromTimeTime = rundata.getQueryString().getDate("fromTimeTime", TIME_FMT);
		Date arriveTimeDate = rundata.getQueryString().getDate("arriveTimeDate", DATE_FMT);
		Date arriveTimeTime = rundata.getQueryString().getDate("arriveTimeTime", TIME_FMT);
		Date fromTime = DateTimeUtil.componentDateAndTime(fromTimeDate, fromTimeTime);
		Date arriveTime = DateTimeUtil.componentDateAndTime(arriveTimeDate, arriveTimeTime);
		
		orderDO.setFromTime(fromTime);
		orderDO.setArriveTime(arriveTime);
		
		Result result = orderAO.createTaxiOrder(orderDO);
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
