package com.aifuyun.snow.world.biz.bo.search;




import com.aifuyun.snow.world.biz.query.search.SortField;
import com.aifuyun.snow.world.biz.query.search.FieldOrder;



import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.test.BaseTest;

public class OrderSearchBOTest extends BaseTest {

	private OrderSearchBO orderSearchBO;

	public void testSearch() {
		SearchQuery query = new SearchQuery();
		query.setQ("*:*");
		
		SortField sf = new SortField("fromTime", FieldOrder.DESC);
		query.setSortFields(CollectionUtil.asList(sf));
		SearchResult<SearchOrderDO> result = orderSearchBO.queryOrders(query);
		for (OrderDO order : result.getResult()) {
			System.out.println(order.getId() + ", " + order.getFromCity() + ", " + order.getArriveAddr() 
					+ " gmt_create:" + order.getGmtCreate() + ", create_order_time:" + order.getFromTime());
		} 
		
		
	}
	
	public void setOrderSearchBO(OrderSearchBO orderSearchBO) {
		this.orderSearchBO = orderSearchBO;
	}
	
}
