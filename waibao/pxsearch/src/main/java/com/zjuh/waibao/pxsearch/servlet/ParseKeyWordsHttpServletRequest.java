package com.zjuh.waibao.pxsearch.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.zjuh.sweet.lang.StringUtil;

public class ParseKeyWordsHttpServletRequest extends HttpServletRequestWrapper {

	private String field = "q";
	
	private String queryValue;
	
	public ParseKeyWordsHttpServletRequest(HttpServletRequest request) {
		super(request);
		String q = request.getParameter("q");
		if (StringUtil.isEmpty(q)) {
			queryValue = "*:*";
		} else {
			queryValue = SearchUtil.parseItemKeywords("title", "tags", "content", q);
		}
	}

	@Override
	public String getParameter(String name) {
		if (field.equals(name)) {
			return queryValue;
		}
		return super.getParameter(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getParameterMap() {
		Map ret = new HashMap();
		ret.putAll(super.getParameterMap());
		ret.put(field, new String[] {queryValue});
		return ret;
	}

	@Override
	public String[] getParameterValues(String name) {
		if (field.equals(name)) {
			return new String[] {queryValue};
		}
		return super.getParameterValues(name);
	}
	
	

}
