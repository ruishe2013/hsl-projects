package com.aifuyun.snow.search.orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.util.StringUtil;
import com.aifuyun.snow.search.util.ConnectionPool;
import com.aifuyun.snow.search.util.SqlUtil;

public abstract class BaseDataProvider implements DataProvider {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private ConnectionPool connectionPool = new ConnectionPool();
	
	private Connection connection;
	
	private PreparedStatement pstmt;
	
	private ResultSet rs;
	
	private boolean inited;
	
	@Override
	public void close() {
		SqlUtil.close(rs);
		SqlUtil.close(pstmt);
		SqlUtil.close(connection);
		rs = null;
		pstmt = null;
		connection = null;
		inited = false;
	}
	
	protected abstract String getSqlCondition();

	@Override
	public boolean hasNext() {
		if (!inited) {
			return false;
		}
		try {
			return rs.next();
		} catch (SQLException e) {
			log.error("rs next failed", e);
			return false;
		}
	}

	@Override
	public void init() {
		connectionPool.init();
		connection = connectionPool.getConnection();
		String sql = genSql();
		try {
			log.info("sql: " + sql);
			pstmt = connection.prepareStatement(sql);
			pstmt.setFetchSize(1);
			rs = pstmt.executeQuery();
			inited = true;
		} catch (SQLException e) {
			log.error("≥ı ºªØ ß∞‹", e);
		}
	}

	@Override
	public Map<String, String> next() {
		if (!inited) {
			return null;
		}
		Map<String, String> ret = new HashMap<String, String>();
		try {
			ret.put("id", rs.getString("id"));
			ret.put("creatorId", rs.getString("creator_id"));
			ret.put("creatorUsername", rs.getString("creator_username"));
			ret.put("cityId", rs.getString("city_id"));
			ret.put("fromCity", rs.getString("from_city"));
			ret.put("arriveCityId", rs.getString("arrive_city_id"));
			ret.put("arriveCity", rs.getString("arrive_city"));
			ret.put("fromAddr", rs.getString("from_addr"));
			ret.put("arriveAddr", rs.getString("arrive_addr"));
	//		ret.put("fromDate", rs.getString("from_time"));
	//		ret.put("arriveDate", rs.getString("arrive_time"));
			ret.put("description", rs.getString("description"));
			ret.put("approach", rs.getString("approach"));
			ret.put("status", rs.getString("status"));
			ret.put("type", rs.getString("type"));
			ret.put("deleted", rs.getString("deleted"));
			System.out.println(ret);
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}


	private String genSql() {
		String sql = "select t.id, t.city_id, t.gmt_modified, t.from_addr, t.gmt_create, t.creator_username, t.deleted,"+
			"t.creator_id, t.from_time, t.type, t.id, t.total_seats," +
		 "t.arrive_time, t.description, t.from_city, t.arrive_city, t.arrive_addr, " +
		 "t.status, t.approach, t.arrive_city_id from sw_order t ";
		String condition = getSqlCondition();
		if (StringUtil.isEmpty(condition)) {
			return sql;
		}
		return sql + " where " + condition;
	}
	
	
}
