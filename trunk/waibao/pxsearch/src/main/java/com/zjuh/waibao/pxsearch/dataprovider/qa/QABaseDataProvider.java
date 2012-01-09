package com.zjuh.waibao.pxsearch.dataprovider.qa;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Map;

import com.zjuh.waibao.pxsearch.dataprovider.CommonDataProvider;
import com.zjuh.waibao.pxsearch.util.WordUtil;

public class QABaseDataProvider extends CommonDataProvider {

	@Override
	protected String getSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSqlCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, String> mappingResult(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected String getWordContent(InputStream is) {
		return WordUtil.getWordText(is);
	}
	
}
