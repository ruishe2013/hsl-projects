package com.aifuyun.snow.world.biz.bo.search;

import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;

public interface OrderSearchBO {
	
	SearchResult<SearchOrderDO> queryOrders(SearchQuery query);

}
