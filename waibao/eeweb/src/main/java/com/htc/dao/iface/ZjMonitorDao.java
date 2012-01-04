package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.ZjMonitors;

public interface ZjMonitorDao {
	
	public int test4Link() throws Exception;
	
	// 添加或者修改仪器信息
	public List<ZjMonitors> selectAllZjMonitor() throws Exception;
	
	public void insertZjMonitor(ZjMonitors zjMoni)throws Exception;
	
	public void updateMonitor(ZjMonitors zjMoni) throws Exception;
	
	public void deleteMonitor(ZjMonitors zjMoni) throws Exception;
	
	public void doBatchMonitor(List<ZjMonitors> zjMoniList,int batchType) throws Exception;
	
}
