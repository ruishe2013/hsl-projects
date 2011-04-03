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
			// ����ɹ�
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
		Date fromTimeTime = rundata.getQueryString().getDate("fromTimeTime", TIME_FMT);
		Date arriveTimeDate = rundata.getQueryString().getDate("arriveTimeDate", DATE_FMT);
		Date arriveTimeTime = rundata.getQueryString().getDate("arriveTimeTime", TIME_FMT);
		
		Date fromTime = DateTimeUtil.componentDateAndTime(fromTimeDate, fromTimeTime);
		
		Date arriveTime = null;
		if (arriveTimeDate != null) {
			if (arriveTimeTime != null) {
				arriveTime = DateTimeUtil.componentDateAndTime(arriveTimeDate, arriveTimeTime);
			} else {
				arriveTime = arriveTimeDate;
			}
		} else {
			// ��ʱ�ѳ���ʱ�䵱������ʱ�䴦��
			//arriveTime = fromTimeDate;
		}
		
		orderDO.setFromTime(fromTime);
		orderDO.setArriveTime(arriveTime);
		
		// Ŀ�ĳ��кͳ���������ͬһ��
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
