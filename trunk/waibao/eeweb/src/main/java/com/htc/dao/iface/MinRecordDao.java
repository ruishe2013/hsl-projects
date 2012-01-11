package com.htc.dao.iface;

import java.util.*;

import com.htc.bean.BeanForPortData;

public interface MinRecordDao {

	public int getCounts() throws Exception;
	
	public List<BeanForPortData> getNewestRec(String userPlaceList) throws Exception;
	
	public long lastRecTime() throws Exception;
	
	public long earlyRecTime() throws Exception;
	
	public List<BeanForPortData> selectAllRec(String userPlaceList) throws Exception;
	
	public void insertRecord(Map<Integer, BeanForPortData> records, int repeat) throws Exception;
	
	public void updateRecord(Map<Integer, BeanForPortData> records) throws Exception;
	
	public void deleteAll() throws Exception;
	
	public void truncateRecord() throws Exception;
	
	public List<BeanForPortData> getNewestRecAll()  throws Exception;
	
}
