package com.aifuyun.snow.search;

import com.aifuyun.search.core.DataProviderFactory;
import com.aifuyun.search.servlet.XSolrDispatchFilter;
import com.aifuyun.snow.search.orders.OrderDataProviderFactory;

public class SnowWorldSolrDispatchFilter extends XSolrDispatchFilter {

	private DataProviderFactory dataProviderFactory = new OrderDataProviderFactory();
	
	@Override
	protected DataProviderFactory getDataProviderFactory() {
		return dataProviderFactory;
	}

}
