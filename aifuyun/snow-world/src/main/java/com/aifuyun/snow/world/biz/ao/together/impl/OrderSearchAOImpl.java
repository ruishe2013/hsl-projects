package com.aifuyun.snow.world.biz.ao.together.impl;

import java.util.List;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.bo.search.OrderSearchBO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.common.SearchUtil;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class OrderSearchAOImpl extends BaseAO implements OrderSearchAO {

	private OrderSearchBO orderSearchBO;
	
	@Override
	public Result searchOrder(SearchOrderQuery searchOrderQuery) {
		Result result = new ResultSupport(false);
		try {
			SearchQuery searchQuery = new SearchQuery();
			String q = buildSearchQuery(searchOrderQuery);
			searchQuery.setQ(q);
			searchQuery.setRows(searchOrderQuery.getPageSize());
			SearchResult<OrderDO> searchResult = orderSearchBO.queryOrders(searchQuery);
			long numFound = searchResult.getNumFound();
			List<OrderDO> orders = searchResult.getResult();
			result.getModels().put("numFound", numFound);
			result.getModels().put("orders", orders);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ËÑË÷Æ´³µÊ§°Ü", e);
		}
		return result;
	}

	private String buildSearchQuery(SearchOrderQuery searchOrderQuery) {
		StringBuilder sb = new StringBuilder();
		String arriveAddr = searchOrderQuery.getArriveAddr();
		String cityName = searchOrderQuery.getFromCity();
		String fromAddr = searchOrderQuery.getFromAddr();
		String arriveCity = searchOrderQuery.getArriveCity();
		if (!StringUtil.isEmpty(arriveCity)) {
			sb.append(" +arriveCity:" + SearchUtil.filter(arriveCity));
		}
		if (!StringUtil.isEmpty(arriveAddr)) {
			sb.append(" +arriveAddrText:" + SearchUtil.filter(arriveAddr));
		}
		
		if (!StringUtil.isEmpty(cityName)) {
			sb.append(" +fromCity:" + SearchUtil.filter(cityName));
		}
		
		if (!StringUtil.isEmpty(fromAddr)) {
			sb.append(" +fromAddrText:" + SearchUtil.filter(fromAddr));
		}
		String ret = sb.toString();
		if (StringUtil.isEmpty(ret)) {
			return "*:*";
		}
		return ret;
	}
	
	public void setOrderSearchBO(OrderSearchBO orderSearchBO) {
		this.orderSearchBO = orderSearchBO;
	}

}
