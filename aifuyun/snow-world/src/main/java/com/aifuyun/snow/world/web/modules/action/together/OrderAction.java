package com.aifuyun.snow.world.web.modules.action.together;

import java.util.Date;

import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseAction;
import com.zjuh.splist.core.annotation.DefaultTarget;
import com.zjuh.splist.core.form.Form;
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
			this.sendRedirect("snowModule", "together/personalInfo");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
