package com.aifuyun.snow.world.biz.bo.corp.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.corp.CorpMailBO;
import com.aifuyun.snow.world.dal.daointerface.corp.CorpMailDAO;
import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;

public class CorpMailBOImpl implements CorpMailBO {

	private CorpMailDAO corpMailDAO;

	public int create(CorpMailDO corpMailDO) {
		return corpMailDAO.create(corpMailDO);
	}

	public CorpMailDO queryById(int id) {
		return corpMailDAO.queryById(id);
	}

	public List<CorpMailDO> queryByCorpName(String corpName) {
		return corpMailDAO.queryByCorpName(corpName);
	}

	public CorpMailDO queryByMailHost(String mailHost) {
		return corpMailDAO.queryByMailHost(mailHost);
	}

	public void delete(int id) {
		corpMailDAO.delete(id);
	}

	public void update(CorpMailDO corpMailDO) {
		corpMailDAO.update(corpMailDO);
	}

	public void setCorpMailDAO(CorpMailDAO corpMailDAO) {
		this.corpMailDAO = corpMailDAO;
	}
	
}
