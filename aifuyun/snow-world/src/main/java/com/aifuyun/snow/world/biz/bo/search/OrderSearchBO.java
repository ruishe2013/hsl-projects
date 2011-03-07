package com.aifuyun.snow.world.biz.bo.search;

import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

public interface OrderSearchBO {
	
	SearchResult<OrderDO> queryOrders(SearchQuery query);

}
