package com.zjuh.waibao.pxsearch.dataprovider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aifuyun.search.build.DataProvider;

public class MultiDataProvider implements DataProvider {

	private List<? extends DataProvider> dataProviders;

	private Iterator<? extends DataProvider> dataProviderIterator;
	
	private DataProvider currentDataProvider;
	
	public MultiDataProvider(List<? extends DataProvider> dataProviders) {
		super();
		this.dataProviders = dataProviders;
	}

	@Override
	public void close() {
		if (currentDataProvider != null) {
			currentDataProvider.close();
		}
	}

	@Override
	public boolean hasNext() {
		boolean ret = currentDataProvider.hasNext();
		if (ret) {
			return true;
		}
		
		ret = turnToNextDataProvider();
		while ( !ret ) {
			ret = turnToNextDataProvider();
		}
		return ret;
	}

	@Override
	public void init() {
		if (dataProviders != null) {
			dataProviderIterator = dataProviders.iterator();
		}
		turnToNextDataProvider();
	}

	private boolean turnToNextDataProvider() {
		if (dataProviderIterator == null) {
			return false;
		}
		if (!dataProviderIterator.hasNext()) {
			return false;
		}
		DataProvider nextDataProvider = dataProviderIterator.next();
		if (currentDataProvider != null) {
			// 关闭当前
			currentDataProvider.close();
		}
		// 初始化下一个
		nextDataProvider.init();
		currentDataProvider = nextDataProvider;
		return true;
	}
	
	
	@Override
	public Map<String, String> next() {
		return currentDataProvider.next();
	}

}
