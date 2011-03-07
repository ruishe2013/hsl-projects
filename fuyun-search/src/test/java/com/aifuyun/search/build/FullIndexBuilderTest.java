package com.aifuyun.search.build;

import junit.framework.TestCase;

import com.aifuyun.search.core.CoreContainerHolder;

public class FullIndexBuilderTest extends TestCase {

	private IndexBuilderFactory indexBuilderFactory;
	
	private IndexBuilder builder;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		CoreContainerHolder.createCoreContainer("d:/solr_home", "d:/solr_home/solr.xml");
		indexBuilderFactory = new IndexBuilderFactory();
		MyIncrDataProvider dataProvider = new MyIncrDataProvider();
		builder = indexBuilderFactory.createFullIndexBuilder("search1", dataProvider);
	}

	public void testBuild() {
		builder.build();
	}
	
}
