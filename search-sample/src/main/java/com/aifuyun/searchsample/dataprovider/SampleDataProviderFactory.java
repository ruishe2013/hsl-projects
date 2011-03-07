package com.aifuyun.searchsample.dataprovider;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.core.DataProviderFactory;

public class SampleDataProviderFactory implements DataProviderFactory {

	@Override
	public DataProvider createFullDataProvider(String arg0) {
		return new FullDataProvider();
	}

	@Override
	public DataProvider createIncrDataProvider(String arg0) {
		return new IncrDataProvider();
	}

}
