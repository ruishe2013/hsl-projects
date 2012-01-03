package com.zjuh.waibao.pxsearch;

import com.aifuyun.search.core.DataProviderFactory;
import com.aifuyun.search.servlet.XSolrDispatchFilter;
import com.zjuh.waibao.pxsearch.dataprovider.MultiDataProviderFactory;

public class PxSearchDispatchFilter extends XSolrDispatchFilter {
	
	DataProviderFactory itemSearchDataProviderFactory = new MultiDataProviderFactory();
	
	@Override
	protected DataProviderFactory getDataProviderFactory() {
		return itemSearchDataProviderFactory;
	}

}
