package com.aifuyun.search.core;

import com.aifuyun.search.build.DataProvider;

public interface DataProviderFactory {
	
	DataProvider createFullDataProvider(String coreName);
	
	DataProvider createIncrDataProvider(String coreName);

}
