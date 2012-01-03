package com.zjuh.waibao.pxsearch.dataprovider.cource;



public class FullCourseDataProvider extends BaseCourseDataProvider {

	@Override
	protected String getSqlCondition() {
		return " t.deleted = 0 ";
	}

}
