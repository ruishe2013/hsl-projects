package com.zjuh.waibao.pxsearch.dataprovider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aifuyun.search.build.DataProvider;

public class MultiDataProvider implements DataProvider {

	private List<? extends DataProvider> dataProviders;

	private Iterator<? extends DataProvider> dataProviderIterator;
	
	private DataProvider currentDataProvider;
	
	private boolean hasNextRow = false;
	
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

	private boolean hasNextImpl() {
		boolean ret = currentDataProvider.hasNext();
		if (ret) {
			return true;
		}
		
		while ( true ) {
			ret = turnToNextDataProvider();
			if (!ret) {
				return false;
			}
			if (currentDataProvider.hasNext()) {
				return true;
			} else {
				continue;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		hasNextRow = hasNextImpl();
		return hasNextRow;
	}

	@Override
	public void init() {
		if (dataProviders != null) {
			dataProviderIterator = dataProviders.iterator();
		}
		turnToNextDataProvider();
	}

	private void clearCurrentDataProvider() {
		if (currentDataProvider != null) {
			// 关闭当前
			currentDataProvider.close();
			currentDataProvider = null;
		}
	}
	
	private boolean turnToNextDataProvider() {
		if (dataProviderIterator == null) {
			return false;
		}
		if (!dataProviderIterator.hasNext()) {
			clearCurrentDataProvider();
			return false;
		}
		DataProvider nextDataProvider = dataProviderIterator.next();
		clearCurrentDataProvider();
		// 初始化下一个
		nextDataProvider.init();
		currentDataProvider = nextDataProvider;
		return true;
	}
	
	
	@Override
	public Map<String, String> next() {
		if (!hasNextRow) {
			return null;
		}
		return currentDataProvider.next();
	}

}
