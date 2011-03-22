package com.aifuyun.snow.world.web.common.pagination;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.splist.web.TemplateContext;

public class OrderSearchPageFacer implements PageFacer {

	@Override
	public URLModule gotoPage(int page) {
		URLModuleContainer container = SplistContext.getSplistComponent().getUrlModuleContainers().get("snowModule");
		URLModule urlModule = container.setTarget("search/searchOrder");
		TemplateContext templateContext = SplistContext.getTemplateContext();
		SearchOrderQuery query = (SearchOrderQuery)templateContext.get("query");
		urlModule.addQueryData("page", page);
		
		String fromCity = query.getFromCity();
		if (!StringUtil.isEmpty(fromCity)) {
			urlModule.addQueryData("fromCity", fromCity);
		}
		
		String fromAddr = query.getFromAddr();
		if (!StringUtil.isEmpty(fromAddr)) {
			urlModule.addQueryData("fromAddr", fromAddr);
		}
		
		String arriveCity = query.getArriveCity();
		if (!StringUtil.isEmpty(arriveCity)) {
			urlModule.addQueryData("arriveCity", arriveCity);
		}
		
		String arriveAddr = query.getArriveAddr();
		if (!StringUtil.isEmpty(arriveAddr)) {
			urlModule.addQueryData("arriveAddr", arriveAddr);
		}
		
		long fromTime = query.getFromTime();
		if (fromTime != 0L) {
			urlModule.addQueryData("fromTime", DateTimeUtil.long2Date(fromTime));
		}
		
		long arriveTime = query.getArriveTime();
		if (arriveTime != 0L) {
			urlModule.addQueryData("arriveTime", DateTimeUtil.long2Date(arriveTime));
		}
		
		return urlModule;
	}
	
	

}
