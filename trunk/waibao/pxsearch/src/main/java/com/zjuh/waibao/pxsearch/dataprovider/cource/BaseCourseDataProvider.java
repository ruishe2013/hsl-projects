package com.zjuh.waibao.pxsearch.dataprovider.cource;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.zjuh.waibao.pxsearch.dataprovider.CommonDataProvider;
import com.zjuh.waibao.pxsearch.doc.Types;

public abstract class BaseCourseDataProvider extends CommonDataProvider {

	protected Map<String, String> mappingResult(ResultSet rs) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			
			String pid = rs.getString("PID");
			String type = Types.COURCE;
			
			ret.put("id", makeId(pid, type));
			ret.put("docId", pid);
			
			String titleString = rs.getString("Name");
			titleString += rs.getString("EnName");
			
			ret.put("title", titleString);
			
			String content = rs.getString("Intro");
			content += rs.getString("Description");
			content += rs.getString("PageTitle");
			
			ret.put("content", content);
			ret.put("tags", rs.getString("Keyword"));
			ret.put("type", type);
			
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}
	
	protected String getSql() {
		String sql = "select PID, Name, EnName, Code, Intro, Keyword, Description, PageTitle from c_courses t" +
		" where 1=1 ";
		return sql;
	}
	
	
	
}
