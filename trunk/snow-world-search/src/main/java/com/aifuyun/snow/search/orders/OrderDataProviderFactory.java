package com.aifuyun.snow.search.orders;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.core.DataProviderFactory;

public class OrderDataProviderFactory implements DataProviderFactory {
	
	@Override
	public DataProvider createFullDataProvider(String arg0) {
		return new FullOrderDataProvider();
	}

	private IncrOrderDataProvider incrOrderDataProvider = new IncrOrderDataProvider();
	
	@Override
	public DataProvider createIncrDataProvider(String arg0) {
		return incrOrderDataProvider;
	}

}
