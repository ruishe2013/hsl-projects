package com.aifuyun.snow.world.web.modules.screen.together;

import java.util.Arrays;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;

public class CreateOrderWork extends BaseScreen {

	private OrderAO orderAO;

	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int orderType = rundata.getQueryString().getInt("orderType");
		long orderId = rundata.getQueryString().getLong("orderId", 0L);
		Result result = orderAO.viewCreateOrder(orderId, orderType);
		
		if (result.isSuccess()) {
			final Form form = rundata.getForm("together.createOrderWork");
			OrderDO order = (OrderDO) result.getModels().get("order");
			// result.getModels().put("idEdit", false) 通过isEdit判读是否是编辑操作
			this.result2Context(result, templateContext);

			if (!form.isHeldValues()) {
				form.holdValues(order);
				if(order != null) {
					setFieldValue(form.getFields().get("companyAddr"), order.getArriveAddr());
					setFieldValue(form.getFields().get("fromTimeWeek"), strToList(order.getFromWeek()));
					
					setFieldValue(form.getFields().get("fromTimeHour"), trimZero(DateUtil.formatDate(order.getFromTime(), "HH")));
					setFieldValue(form.getFields().get("fromTimeMinute"), trimZero(DateUtil.formatDate(order.getFromTime(), "mm")));
					
					setFieldValue(form.getFields().get("arriveTimeHour"), trimZero(DateUtil.formatDate(order.getArriveTime(), "HH")));
					setFieldValue(form.getFields().get("arriveTimeMinute"), trimZero(DateUtil.formatDate(order.getArriveTime(), "mm")));
					
					setFieldValue(form.getFields().get("afterWorkFromTimeHour"), trimZero(DateUtil.formatDate(order.getAfterWorkFromTime(), "HH")));
					setFieldValue(form.getFields().get("afterWorkFromTimeMinute"), trimZero(DateUtil.formatDate(order.getAfterWorkFromTime(), "mm")));
		
				}
			}
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}


	private List<String> strToList(String fromWeek) {
		if(fromWeek == null || fromWeek.length() <1) {
			return null;
		}
		String[] strArray = fromWeek.split(",");
		return Arrays.asList(strArray);
	}


	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
