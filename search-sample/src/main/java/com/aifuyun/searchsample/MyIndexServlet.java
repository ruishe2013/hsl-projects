package com.aifuyun.searchsample;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.servlet.AbstractIndexServlet;
import com.aifuyun.searchsample.dataprovider.FullDataProvider;
import com.aifuyun.searchsample.dataprovider.IncrDataProvider;

public class MyIndexServlet extends AbstractIndexServlet {

	private static final long serialVersionUID = 3345884909671554935L;

	@Override
	protected DataProvider getFullDataProvider(String arg0) {
		return new FullDataProvider();
	}

	@Override
	protected DataProvider getIncrDataProvider(String arg0) {
		return new IncrDataProvider();
	}

}
