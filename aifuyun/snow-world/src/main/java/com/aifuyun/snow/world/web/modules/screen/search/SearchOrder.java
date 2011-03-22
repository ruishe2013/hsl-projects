package com.aifuyun.snow.world.web.modules.screen.search;


import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.result.Result;

public class SearchOrder extends BaseScreen {

	private OrderSearchAO orderSearchAO;
	
	private int pageSize = 20;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		int page = rundata.getQueryString().getInt("page", 1);
		String fromCity = rundata.getQueryString().getString("fromCity");
		String fromAddr = rundata.getQueryString().getString("fromAddr");
		String arriveCity = rundata.getQueryString().getString("arriveCity");
		String arriveAddr = rundata.getQueryString().getString("arriveAddr");
		String fromTime = rundata.getQueryString().getString("fromTime");
		String arriveTime = rundata.getQueryString().getString("arriveTime");
		
		SearchOrderQuery searchOrderQuery = new SearchOrderQuery();
		searchOrderQuery.setPageSize(pageSize);
		searchOrderQuery.setPageNo(page);
		
		
		searchOrderQuery.setFromCity(fromCity);
		searchOrderQuery.setFromAddr(fromAddr);
		searchOrderQuery.setFromTime(DateTimeUtil.date2long(fromTime));
		
		searchOrderQuery.setArriveCity(arriveCity);
		searchOrderQuery.setArriveAddr(arriveAddr);
		searchOrderQuery.setArriveTime(DateTimeUtil.date2long(arriveTime));
		
		Result result = orderSearchAO.searchOrder(searchOrderQuery);
		
		Long numFound = (Long)result.getModels().get("numFound");
		
		searchOrderQuery.setTotalResultCount(numFound.intValue());
		
		if (result.isSuccess()) {
			this.result2Context(result, templateContext, "orders");
			templateContext.put("searchOrderQuery", searchOrderQuery);
			templateContext.put("numFound", numFound);
		} else {
			this.handleError(result, rundata, templateContext);
		}
	}

	public void setOrderSearchAO(OrderSearchAO orderSearchAO) {
		this.orderSearchAO = orderSearchAO;
	}

}
