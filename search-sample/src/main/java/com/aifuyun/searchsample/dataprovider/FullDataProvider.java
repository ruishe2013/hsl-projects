package com.aifuyun.searchsample.dataprovider;

import com.aifuyun.search.build.DataProvider;

public class FullDataProvider extends BaseDataProvider implements DataProvider {

	@Override
	protected String getFilePath() {
		return "D:/temp_data/full.txt";
	}


}
