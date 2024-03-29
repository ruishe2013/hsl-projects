package com.aifuyun.snow.world.biz.bo.corp;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;

public interface CorpMailBO {

	int create(CorpMailDO corpMailDO);

	CorpMailDO queryById(int id);

	List<CorpMailDO> queryByCorpName(String corpName);

	CorpMailDO queryByMailHost(String mailHost);

	void delete(int id);

	void update(CorpMailDO corpMailDO);

}
