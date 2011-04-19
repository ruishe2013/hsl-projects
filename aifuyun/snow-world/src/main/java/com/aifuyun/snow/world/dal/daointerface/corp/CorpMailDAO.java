package com.aifuyun.snow.world.dal.daointerface.corp;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;

public interface CorpMailDAO {
	
	int create(CorpMailDO corpMailDO);
	
	CorpMailDO queryById(int id);
	
	List<CorpMailDO> queryByCorpName(String corpName);
	
	CorpMailDO queryByMailHost(String mailHost);
	
	void delete(int id);
	
	void update(CorpMailDO corpMailDO);

}
