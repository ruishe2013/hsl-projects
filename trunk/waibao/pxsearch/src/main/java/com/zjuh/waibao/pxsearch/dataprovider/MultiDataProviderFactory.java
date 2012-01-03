package com.zjuh.waibao.pxsearch.dataprovider;

import java.util.Arrays;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.core.DataProviderFactory;
import com.zjuh.waibao.pxsearch.dataprovider.cource.FullCourseDataProvider;

public class MultiDataProviderFactory implements DataProviderFactory {

	@Override
	public DataProvider createFullDataProvider(String arg0) {
		return new MultiDataProvider(Arrays.asList(new FullCourseDataProvider()));
	}

	@Override
	public DataProvider createIncrDataProvider(String arg0) {
		// return new MultiDataProvider(Arrays.asList(new IncrCourseDataProvider()));
		return null;
	}

}
