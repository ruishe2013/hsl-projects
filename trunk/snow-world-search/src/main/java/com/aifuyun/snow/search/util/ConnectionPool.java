package com.aifuyun.snow.search.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.util.ResouceUtil;

public class ConnectionPool {
	
	private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);
	
	private String url;
	
	private String username;
	
	private String password;
	
	private Connection conn;
	
	public void init() {
		InputStream is = ResouceUtil.getResource("jdbc.properties");
		Properties prop = new Properties();
		try {
			prop.load(is);
			Class.forName(prop.getProperty("jdbc.driver")).newInstance();
			url = prop.getProperty("jdbc.url");
			username = prop.getProperty("jdbc.username");
			password = prop.getProperty("jdbc.password");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			log.error("init failed", e);
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	

}
