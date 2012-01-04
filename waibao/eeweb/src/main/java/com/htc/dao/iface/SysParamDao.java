package com.htc.dao.iface;

import java.util.List;
import java.util.Map;

import com.htc.domain.SysParam;

public interface SysParamDao {

	public List<SysParam> selectAllParam() throws Exception;
	
	public void updateSysParam (Map<String, String> argsMap) throws Exception;
	
	public void updateSysParam(List<SysParam> sysParamList) throws Exception;	

}
