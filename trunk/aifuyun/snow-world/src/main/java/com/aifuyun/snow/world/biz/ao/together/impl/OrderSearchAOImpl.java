package com.aifuyun.snow.world.biz.ao.together.impl;

import java.util.Date;
import java.util.List;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.bo.search.OrderSearchBO;
import com.aifuyun.snow.world.biz.bo.search.SearchOrderDO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.biz.query.search.FieldOrder;
import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.biz.query.search.SortField;
import com.aifuyun.snow.world.common.SearchUtil;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.DateUtil;
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
			searchQuery.setStartRow(searchOrderQuery.getStartRow());
			
			// ������ʱ�䵹����
			SortField sf = new SortField("fromTime", FieldOrder.ASC);
			searchQuery.setSortFields(CollectionUtil.asList(sf));
			
			SearchResult<SearchOrderDO> searchResult = orderSearchBO.queryOrders(searchQuery);
			long numFound = searchResult.getNumFound();
			List<SearchOrderDO> orders = searchResult.getResult();
			result.getModels().put("numFound", numFound);
			result.getModels().put("orders", orders);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("����ƴ��ʧ��", e);
		}
		return result;
	}

	/**
	 * ֻ����������ǰ1��Сʱǰ��1���ڵ�����
	 * @param sb
	 */
	private void filterFromDate(StringBuilder sb) {
		// ����ʱ���Ѿ�����1Сʱ������ݾ�������������
		Date searchDate = DateUtil.addSecond(new Date(), -3600);
		sb.append(" +fromTime:[").append(DateUtil.formatDate(searchDate, "yyyyMMddHHmmss"));
		sb.append(" TO ").append(DateUtil.formatDate(DateUtil.addDay(new Date(), 365), "yyyyMMddHHmmss"));
		sb.append("]");
		
	}
	
	private String buildSearchQuery(SearchOrderQuery searchOrderQuery) {
		StringBuilder sb = new StringBuilder();
		String arriveAddr = searchOrderQuery.getArriveAddr();
		String cityName = searchOrderQuery.getFromCity();
		String fromAddr = searchOrderQuery.getFromAddr();
		String arriveCity = searchOrderQuery.getArriveCity();
		if (!StringUtil.isEmpty(arriveCity)) {
			sb.append(" +arriveCity:").append(SearchUtil.filter(arriveCity));
		}
		if (!StringUtil.isEmpty(arriveAddr)) {
			sb.append(" +arriveAddrText:").append(SearchUtil.filter(arriveAddr));
		}
		
		if (!StringUtil.isEmpty(cityName)) {
			sb.append(" +fromCity:").append(SearchUtil.filter(cityName));
		}
		
		if (!StringUtil.isEmpty(fromAddr)) {
			sb.append(" +fromAddrText:").append(SearchUtil.filter(fromAddr));
		}
		
		if (!searchOrderQuery.isIgnoreStartFromTime()) {
			// ������ʼʱ��
			filterFromDate(sb);
		}
		
		long fromTime = searchOrderQuery.getFromTime();
		if (fromTime > 0L) {
			sb.append(" +fromTime_ymd:").append(fromTime);
		}
		
		
		// ������ɾ����
		sb.append(" +deleted:0");
		
		
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
