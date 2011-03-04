package com.aifuyun.snow.world.dal.ibatis.area;

import java.util.List;

import com.aifuyun.snow.world.dal.daointerface.area.CityDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class CityDAOIbatis extends BaseIbatisDAO implements CityDAO {

	@Override
	public int create(CityDO cityDO) {
		return (Integer)this.getSqlMapClientTemplate().insert("CityDAO.create", cityDO);
	}

	@Override
	public void update(CityDO cityDO) {
		getSqlMapClientTemplate().update("CityDAO.update", cityDO);
	}

	@Override
	public CityDO queryById(int id) {
		return (CityDO)getSqlMapClientTemplate().queryForObject("CityDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CityDO> queryByProvinceId(int provinceId) {
		return (List<CityDO>)getSqlMapClientTemplate().queryForList("CityDAO.queryByProvinceId", provinceId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CityDO> queryByProvinceIds(List<Integer> provinceIds) {
		return (List<CityDO>)getSqlMapClientTemplate().queryForList("CityDAO.queryByProvinceIds", provinceIds);
	}

	@Override
	public void delete(int id) {
		getSqlMapClientTemplate().update("CityDAO.delete", id);
	}

}
