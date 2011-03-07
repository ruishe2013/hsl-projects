package com.aifuyun.snow.world.biz.bo.search.impl;

import org.apache.solr.common.SolrDocument;

import com.zjuh.sweet.lang.ConvertUtil;

public class SimpleSolrDocument {
	
	private SolrDocument solrDocument;

	public SimpleSolrDocument(SolrDocument solrDocument) {
		super();
		this.solrDocument = solrDocument;
	}
	
	public String getStringValue(String name) {
		Object obj = solrDocument.getFieldValue(name);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}
	
	public int getIntValue(String name) {
		return getIntValue(name, 0);
	}
	
	public int getIntValue(String name, int defaultValue) {
		return ConvertUtil.toInt(getStringValue(name), defaultValue);
	}
	
	public long getLongValue(String name) {
		return getLongValue(name, 0L);
	}
	
	public long getLongValue(String name, long defaultValue) {
		return ConvertUtil.toLong(getStringValue(name), defaultValue);
	}

}
