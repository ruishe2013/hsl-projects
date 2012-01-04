package com.htc.dao;

import java.sql.SQLException;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
//        String userId=(String)DbContextHolder.getContext();    
//        Integer dataSourceId=getDataSourceIdByUserId(userId);           
//        return dataSourceId;    		
		return null;
	}

//	public boolean isWrapperFor(Class<?> iface) throws SQLException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public <T> T unwrap(Class<T> iface) throws SQLException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
