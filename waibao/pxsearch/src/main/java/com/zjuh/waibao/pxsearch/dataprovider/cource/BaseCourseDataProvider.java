package com.zjuh.waibao.pxsearch.dataprovider.cource;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.zjuh.waibao.pxsearch.dataprovider.CommonDataProvider;

public abstract class BaseCourseDataProvider extends CommonDataProvider {

	protected Map<String, String> mappingResult(ResultSet rs) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			ret.put("id", rs.getString("id"));
			ret.put("catId", rs.getString("cat_id"));
			ret.put("phone", rs.getString("phone"));
			ret.put("userId", rs.getString("user_id"));
			ret.put("status", rs.getString("status"));
			ret.put("longitude", rs.getString("longitude"));
			ret.put("latitude", rs.getString("latitude"));
			ret.put("altitude", rs.getString("altitude"));
			ret.put("price", rs.getString("price"));
			ret.put("originalPrice", rs.getString("original_price"));
			ret.put("name", rs.getString("name"));
			ret.put("description", rs.getString("description"));
			ret.put("defaultImage", rs.getString("default_image"));
			
			ret.put("deleted", rs.getString("deleted"));
			ret.put("gmtCreate", fmtDateFull(rs.getTimestamp("gmt_create")));
			ret.put("gmtModified", fmtDateFull(rs.getTimestamp("gmt_modified")));
			
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}
	
	protected String getSql() {
		String sql = "select t.default_image, t.user_id, t.gmt_modified, t.phone, t.gmt_create, t.status, t.altitude, t.id, t.price, " +
		" t.description, t.cat_id, t.longitude, t.latitude, t.deleted, t.name, t.original_price from water_item t" +
		" where 1=1 ";
		return sql;
	}
	
	
	
}
