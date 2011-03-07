package com.aifuyun.search.servlet;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.solr.servlet.SolrDispatchFilter;

import com.aifuyun.search.core.CoreContainerHolder;

public class XSolrDispatchFilter extends SolrDispatchFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		CoreContainerHolder.setCoreContainer(this.cores);
	}


}
