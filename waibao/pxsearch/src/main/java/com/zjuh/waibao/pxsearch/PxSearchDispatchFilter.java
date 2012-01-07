package com.zjuh.waibao.pxsearch;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.aifuyun.search.core.DataProviderFactory;
import com.aifuyun.search.servlet.XSolrDispatchFilter;
import com.zjuh.waibao.pxsearch.dataprovider.MultiDataProviderFactory;
import com.zjuh.waibao.pxsearch.servlet.ParseKeyWordsHttpServletRequest;

public class PxSearchDispatchFilter extends XSolrDispatchFilter {
	
	DataProviderFactory itemSearchDataProviderFactory = new MultiDataProviderFactory();
	
	@Override
	protected DataProviderFactory getDataProviderFactory() {
		return itemSearchDataProviderFactory;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		super.doFilter(parseKeyWords((HttpServletRequest)request), response, chain);
	}
	
	private HttpServletRequest parseKeyWords(HttpServletRequest request) {
		return new ParseKeyWordsHttpServletRequest(request);		
	}

}
