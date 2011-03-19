package com.aifuyun.snow.world.web.modules.screen.search;


import java.util.Date;

import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.ConvertUtil;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;

public class SearchOrder extends BaseScreen {

	private OrderSearchAO orderSearchAO;
	
	private static long date2long(String dateString) {
		Date date = DateUtil.parseDate(dateString, "yyyy-MM-dd");
		if (date == null) {
			return 0L;
		}
		return ConvertUtil.toLong(DateUtil.formatDate(date, "yyyyMMdd"), 0L);
	}
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String fromCity = rundata.getQueryString().getString("fromCity");
		String fromAddr = rundata.getQueryString().getString("fromAddr");
		String arriveCity = rundata.getQueryString().getString("arriveCity");
		String arriveAddr = rundata.getQueryString().getString("arriveAddr");
		String fromTime = rundata.getQueryString().getString("fromTime");
		String arriveTime = rundata.getQueryString().getString("arriveTime");
		
		SearchOrderQuery searchOrderQuery = new SearchOrderQuery();
		
		searchOrderQuery.setFromCity(fromCity);
		searchOrderQuery.setFromAddr(fromAddr);
		searchOrderQuery.setFromTime(date2long(fromTime));
		
		searchOrderQuery.setArriveCity(arriveCity);
		searchOrderQuery.setArriveAddr(arriveAddr);
		searchOrderQuery.setArriveTime(date2long(arriveTime));
		
		Result result = orderSearchAO.searchOrder(searchOrderQuery);
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "orders");
			this.result2Context(result, templateContext, "numFound");
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderSearchAO(OrderSearchAO orderSearchAO) {
		this.orderSearchAO = orderSearchAO;
	}

}
