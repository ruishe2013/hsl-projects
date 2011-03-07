package com.aifuyun.snow.world.web.modules.screen.search;

import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class SearchOrder extends BaseScreen {

	private OrderSearchAO orderSearchAO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String fromCity = rundata.getQueryString().getString("fromCity");
		String fromAddr = rundata.getQueryString().getString("fromAddr");
		String arriveAddr = rundata.getQueryString().getString("arriveAddr");
		
		SearchOrderQuery searchOrderQuery = new SearchOrderQuery();
		searchOrderQuery.setArriveAddr(arriveAddr);
		searchOrderQuery.setFromCity(fromCity);
		searchOrderQuery.setFromAddr(fromAddr);
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
