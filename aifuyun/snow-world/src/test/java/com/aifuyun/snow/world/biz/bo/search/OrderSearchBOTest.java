package com.aifuyun.snow.world.biz.bo.search;

import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.test.BaseTest;

public class OrderSearchBOTest extends BaseTest {

	private OrderSearchBO orderSearchBO;

	public void testSearch() {
		SearchQuery query = new SearchQuery();
		query.setQ("arriveCity:Äþ²¨");
		SearchResult<OrderDO> result = orderSearchBO.queryOrders(query);
		for (OrderDO order : result.getResult()) {
			System.out.println(order.getId() + ", " + order.getFromCity() + ", " + order.getArriveAddr());
		} 
		
		
	}
	
	public void setOrderSearchBO(OrderSearchBO orderSearchBO) {
		this.orderSearchBO = orderSearchBO;
	}
	
}
