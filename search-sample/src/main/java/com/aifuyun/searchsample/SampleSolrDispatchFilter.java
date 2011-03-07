package com.aifuyun.searchsample;

import com.aifuyun.search.core.DataProviderFactory;
import com.aifuyun.search.servlet.XSolrDispatchFilter;
import com.aifuyun.searchsample.dataprovider.SampleDataProviderFactory;

public class SampleSolrDispatchFilter extends XSolrDispatchFilter {

	private SampleDataProviderFactory sampleDataProviderFactory = new SampleDataProviderFactory();
	
	@Override
	protected DataProviderFactory getDataProviderFactory() {
		return sampleDataProviderFactory;
	}

}
