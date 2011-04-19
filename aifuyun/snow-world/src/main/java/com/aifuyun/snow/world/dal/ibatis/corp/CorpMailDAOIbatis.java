package com.aifuyun.snow.world.dal.ibatis.corp;

import java.util.List;

import com.aifuyun.snow.world.dal.daointerface.corp.CorpMailDAO;
import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class CorpMailDAOIbatis extends BaseIbatisDAO implements CorpMailDAO {

	@Override
	public int create(CorpMailDO corpMailDO) {
		return (Integer)this.getSqlMapClientTemplate().insert("CorpMailDAO.create", corpMailDO);
	}

	@Override
	public CorpMailDO queryById(int id) {
		return (CorpMailDO)getSqlMapClientTemplate().queryForObject("CorpMailDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CorpMailDO> queryByCorpName(String corpName) {
		return (List<CorpMailDO>)getSqlMapClientTemplate().queryForList("CorpMailDAO.queryByCorpName", corpName);
	}

	@Override
	public CorpMailDO queryByMailHost(String mailHost) {
		return (CorpMailDO)getSqlMapClientTemplate().queryForObject("CorpMailDAO.queryByMailHost", mailHost);
	}

	@Override
	public void delete(int id) {
		getSqlMapClientTemplate().update("CorpMailDAO.delete", id);
	}

	@Override
	public void update(CorpMailDO corpMailDO) {
		getSqlMapClientTemplate().update("CorpMailDAO.update", corpMailDO);
	}

}
