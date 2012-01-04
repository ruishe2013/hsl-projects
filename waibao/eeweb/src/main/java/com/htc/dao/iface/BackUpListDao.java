package com.htc.dao.iface;

import com.htc.domain.BackUpList;

public interface BackUpListDao {
	
	public BackUpList findBackUpList(BackUpList backUpList)throws Exception;

	public String getLastTime() throws Exception;
	
	public void insertBackUpList(BackUpList backUpList) throws Exception;

	public void updateBackUpList(BackUpList backUpList) throws Exception;

	public void deleteBackUpListById(int backId) throws Exception;
	
	public void deleteBatch(int[] backIds) throws Exception;
}
