package com.zjuh.waibao.pxsearch.dataprovider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.util.ConnectionUtil;
import com.aifuyun.search.util.DateUtil;
import com.aifuyun.search.util.SqlUtil;
import com.aifuyun.search.util.StringUtil;

public abstract class CommonDataProvider implements DataProvider {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
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
		connection = ConnectionUtil.getConnection();
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

	protected abstract Map<String, String> mappingResult(ResultSet rs);
	
	protected abstract String getSql();
	
	@Override
	public Map<String, String> next() {
		if (!inited) {
			return null;
		}
		Map<String, String> ret = mappingResult(rs);
		return ret;
	}
	
	protected static String fmtDateFull(Date date) {
		if (date == null) {
			return null;
		}
		return DateUtil.formatDate(date, "yyyyMMddHHmmss");
	}

	private String genSql() {
		String sql = getSql();
		String condition = getSqlCondition();
		if (StringUtil.isEmpty(condition)) {
			return sql;
		}
		return sql + " and " + condition;
	}
	
	
}
