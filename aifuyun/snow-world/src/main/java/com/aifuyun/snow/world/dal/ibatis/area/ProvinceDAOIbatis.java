package com.aifuyun.snow.world.dal.ibatis.area;

import java.util.List;

import com.aifuyun.snow.world.dal.daointerface.area.ProvinceDAO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class ProvinceDAOIbatis extends BaseIbatisDAO implements ProvinceDAO {

	@Override
	public int create(ProvinceDO provinceDO) {
		return (Integer)this.getSqlMapClientTemplate().insert("ProvinceDAO.create", provinceDO);
	}

	@Override
	public void delete(int id) {
		getSqlMapClientTemplate().update("ProvinceDAO.delete", id);
	}

	@Override
	public void update(ProvinceDO provinceDO) {
		getSqlMapClientTemplate().update("ProvinceDAO.update", provinceDO);
	}

	@Override
	public ProvinceDO queryById(int id) {
		return (ProvinceDO)getSqlMapClientTemplate().queryForObject("ProvinceDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProvinceDO> queryAll() {
		return (List<ProvinceDO>)getSqlMapClientTemplate().queryForList("ProvinceDAO.queryAll");
	}

}
