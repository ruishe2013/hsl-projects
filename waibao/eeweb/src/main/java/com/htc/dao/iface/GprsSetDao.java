package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.GprsSet;

public interface GprsSetDao {

	public List<GprsSet> findAllSetList() throws Exception;

	public List<GprsSet> findGprsSet(GprsSet gprsSet) throws Exception;

	public void insertGprsSet(GprsSet gprsSet) throws Exception;
	
	public void insertEquipDataBatch(List<GprsSet> gprsSetList)throws Exception;

	public void updateGprsSet(GprsSet gprsSet) throws Exception;

	public void deleteGprsSet(int gprsSetId) throws Exception;

	public void deleteBatch(int[] gprsSetIds) throws Exception;

}
