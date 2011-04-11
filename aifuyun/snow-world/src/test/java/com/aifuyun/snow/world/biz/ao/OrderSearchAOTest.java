package com.aifuyun.snow.world.biz.ao;

import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.test.BaseTest;

public class OrderSearchAOTest extends BaseTest {
	
	private OrderSearchAO orderSearchAO;

	public void setOrderSearchAO(OrderSearchAO orderSearchAO) {
		this.orderSearchAO = orderSearchAO;
	}
	
	public void testXX() {
		SearchOrderQuery searchOrderQuery = new SearchOrderQuery();
		
		Result result = orderSearchAO.searchOrder(searchOrderQuery);
		Long numFound = (Long)result.getModels().get("numFound");
		System.out.println(numFound);
		System.out.println(result.getModels().get("orders"));
	}

}
