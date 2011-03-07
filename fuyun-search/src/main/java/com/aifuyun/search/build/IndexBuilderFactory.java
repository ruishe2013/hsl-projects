package com.aifuyun.search.build;

public class IndexBuilderFactory {

	public IndexBuilderFactory() {
		super();
	}

	public IndexBuilder createIncrIndexBuilder(String coreName, DataProvider dataProvider) {
		IncrIndexBuilder ret = new IncrIndexBuilder();
		ret.setDataProvider(dataProvider);
		ret.setCoreName(coreName);
		return ret;
	}
	
	public IndexBuilder createFullIndexBuilder(String coreName, DataProvider dataProvider) {
		FullIndexBuilder ret = new FullIndexBuilder();
		ret.setDataProvider(dataProvider);
		ret.setCoreName(coreName);
		return ret;
	}
	
}
