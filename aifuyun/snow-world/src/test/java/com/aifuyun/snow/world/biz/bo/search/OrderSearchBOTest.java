package com.aifuyun.snow.world.biz.bo.search;




import java.util.Date;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.query.search.FieldOrder;
import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.biz.query.search.SortField;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.test.BaseTest;

public class OrderSearchBOTest extends BaseTest {

	private OrderSearchBO orderSearchBO;

	public void testSearch() {
		SearchQuery query = new SearchQuery();
		String q = buildSearchQuery();
		System.out.println(q);
		query.setQ(q);
		
		SortField sf = new SortField("fromTime", FieldOrder.DESC);
		query.setSortFields(CollectionUtil.asList(sf));
		SearchResult<SearchOrderDO> result = orderSearchBO.queryOrders(query);
		for (OrderDO order : result.getResult()) {
			System.out.println(order.getId() + ", " + order.getFromCity() + ", " + order.getArriveAddr() 
					+ " gmt_create:" + order.getGmtCreate() + ", create_order_time:" + order.getFromTime());
		} 
		
		
	}
	
	private String buildSearchQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append(" +fromTime:[").append(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
		sb.append(" TO ").append(DateUtil.formatDate(DateUtil.addDay(new Date(), 100), "yyyyMMddHHmmss"));
		sb.append("]");
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
