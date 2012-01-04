package com.htc.dao.iface;

import java.util.List;

import com.htc.domain.Data4Access;

public interface TAccessDao {

	public List<Data4Access> getlist() throws Exception;
	
	public void insertData(Data4Access data4Access) throws Exception;
	public void insertBatch(List<Data4Access> datas)throws Exception;

	public void updateData(Data4Access data4Access) throws Exception;
	public void updateBatch(List<Data4Access> datas) throws Exception;

	public void deleteAll() throws Exception;
	public void deleteDataById(String strDSRSN) throws Exception;
	public void deleteBatch(String[] strDSRSNs) throws Exception;

}
