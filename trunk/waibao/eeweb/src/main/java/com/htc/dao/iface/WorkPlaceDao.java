package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.Workplace;

public interface WorkPlaceDao {

	public List<Workplace> findAllWorkplace(int useless) throws Exception;
	
	public Workplace findWorkplace(Workplace workplace) throws Exception;

	public void insertWorkplace(Workplace workplace) throws Exception;
	
	public void insertworkplaceBatch(List<Workplace> worklist)throws Exception;	

	public void updateWorkplace(Workplace workplace) throws Exception;

	public int getChlidCount(String placeName) throws Exception;
	
	public void deleteWorkplace(String placeName) throws Exception;

	public void deleteBatch(String[] names) throws Exception;

}
