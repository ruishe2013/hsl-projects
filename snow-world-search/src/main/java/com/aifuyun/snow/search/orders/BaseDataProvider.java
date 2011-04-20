package com.aifuyun.snow.search.orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.util.DateUtil;
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

	private static String fmtDateYMD(Date date) {
		if (date == null) {
			return null;
		}
		return DateUtil.formatDate(date, "yyyyMMdd");
	}
	
	private static String fmtDateFull(Date date) {
		if (date == null) {
			return null;
		}
		return DateUtil.formatDate(date, "yyyyMMddHHmmss");
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
			ret.put("fromTime_ymd", fmtDateYMD(rs.getTimestamp("from_time")));
			ret.put("arriveTime_ymd", fmtDateYMD(rs.getTimestamp("arrive_time")));
			ret.put("fromTime", fmtDateFull(rs.getTimestamp("from_time")));
			ret.put("arriveTime", fmtDateFull(rs.getTimestamp("arrive_time")));
			ret.put("description", rs.getString("description"));
			ret.put("approach", rs.getString("approach"));
			ret.put("status", rs.getString("status"));
			ret.put("type", rs.getString("type"));
			ret.put("gmtCreate", fmtDateFull(rs.getTimestamp("gmt_create")));
			ret.put("gmtModified", fmtDateFull(rs.getTimestamp("gmt_modified")));
			ret.put("deleted", rs.getString("deleted"));
			ret.put("totalSeats", rs.getString("total_seats"));
			ret.put("creatorRealName", rs.getString("real_name"));
			ret.put("creatorCarOwnerType", rs.getString("creator_car_owner_type"));
			ret.put("carNo", rs.getString("car_no"));
			
			ret.put("fromWeek", rs.getString("from_week"));
			ret.put("carType", rs.getString("car_type"));
			ret.put("afterWorkFromTime", fmtDateFull(rs.getTimestamp("after_work_from_time")));

			ret.put("fromAddrText", createFromAddrText(ret));
			ret.put("arriveAddrText", createArriveAddrText(ret));
		} catch (Exception e) {
			log.error("≤È—Ø ß∞‹", e);
		}
		return ret;
	}

	private String createFromAddrText(Map<String, String> fields) {
		StringBuilder sb = new StringBuilder();
		sb.append(fields.get("fromCity"));
		sb.append(fields.get("fromAddr"));
		sb.append(fields.get("approach"));
		return sb.toString();
	}
	
	private String createArriveAddrText(Map<String, String> fields) {
		StringBuilder sb = new StringBuilder();
		sb.append(fields.get("arriveCity"));
		sb.append(fields.get("arriveAddr"));
		sb.append(fields.get("approach"));
		return sb.toString();
	}
	

	private String genSql() {
		String sql = "select t.id, t.city_id, t.gmt_modified, t.from_addr, t.gmt_create, t.creator_username, t.deleted,"+
			"t.creator_id, t.from_time, t.type, t.id, t.total_seats," +
		 "t.arrive_time, t.description, t.from_city, t.arrive_city, t.arrive_addr, t.creator_car_owner_type, t.car_no, " +
		 "t.status, t.approach, t.arrive_city_id, t.from_week, t.car_type, t.after_work_from_time, u.real_name from sw_order t, sw_order_user u " +
		 " where t.id = u.order_id and t.creator_id = u.user_id and u.deleted = 0 ";
		String condition = getSqlCondition();
		if (StringUtil.isEmpty(condition)) {
			return sql;
		}
		return sql + " and " + condition;
	}
	
	
}
