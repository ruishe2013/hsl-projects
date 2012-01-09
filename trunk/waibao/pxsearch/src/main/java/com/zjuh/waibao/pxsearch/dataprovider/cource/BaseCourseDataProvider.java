package com.zjuh.waibao.pxsearch.dataprovider.cource;

import java.sql.ResultSet;
import java.sql.SQLException;
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
			
			StringBuilder sbTitle = new StringBuilder();
			appendIfNotNull(sbTitle, rs, "Name");
			appendIfNotNull(sbTitle, rs, "EnName");
			
			
			StringBuilder sbContent = new StringBuilder();
			appendIfNotNull(sbContent, rs, "Intro");
			appendIfNotNull(sbContent, rs, "Description");
			appendIfNotNull(sbContent, rs, "PageTitle");
			String content = sbContent.toString();
			
			String tags = rs.getString("Keyword");
			
			ret.put("title", sbTitle.toString());
			ret.put("content", content);
			ret.put("tags", tags);
			
			ret.put("type", type);
			
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}
	
	private void appendIfNotNull(StringBuilder sb, ResultSet rs, String field) throws SQLException {
		String value = rs.getString(field);
		if (value != null) {
			sb.append(" ");
			sb.append(value);
		}
	}
	
	protected String getSql() {
		String sql = "select PID, Name, EnName, Code, Intro, Keyword, Description, PageTitle from c_courses t" +
		" where 1=1 ";
		return sql;
	}
	
	
	
}
