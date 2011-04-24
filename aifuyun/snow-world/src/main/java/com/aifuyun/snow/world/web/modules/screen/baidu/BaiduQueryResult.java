package com.aifuyun.snow.world.web.modules.screen.baidu;

import java.util.StringTokenizer;

import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.StringUtil;

/**
 * @author pister
 *
 */
public class BaiduQueryResult extends BaseScreen {

	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String query = rundata.getQueryString().getString("query");
		this.log.warn("baidu query:" + query);
		SearchParam searchParam = parseSearchParam(query);
		URLModule searchModule = this.getURLModuleContainer("snowModule").setTarget("search/searchOrder");
		searchModule.add("fromAddr", searchParam.from);
		searchModule.add("arriveAddr", searchParam.to);
		this.sendRedirectUrl(searchModule.render());
		
	}
	
	protected SearchParam parseSearchParam(String query) {
		SearchParam searchParam = new SearchParam();
		if (StringUtil.isEmpty(query)) {
			return searchParam;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(query, " \r\n\tµ½");
		if (stringTokenizer.countTokens() <= 1) {
			searchParam.from = query;
			return searchParam;
		}
		String firstToken = null;
		String lastToken = null;
		StringBuilder mToken = new StringBuilder();
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (firstToken == null) {
				firstToken = token;
			} else {
				lastToken = token;
			}
			mToken.append(token);			
		}
		
		searchParam.from = firstToken + mToken;
		searchParam.to = lastToken + mToken;
		
		return searchParam;
	}
	
	static class SearchParam {
		String from;
		String to;
	}

}
