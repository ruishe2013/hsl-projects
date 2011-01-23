package com.aifuyun.snow.world.dal.ibatis.area;

import com.aifuyun.snow.world.dal.daointerface.area.CityIpDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class CityIpDAOIbatis extends BaseIbatisDAO implements CityIpDAO {

	@Override
	public long create(CityIpDO cityIpDO) {
		return (Long)this.getSqlMapClientTemplate().insert("CityIpDAO.create", cityIpDO);
	}

	@Override
	public CityIpDO queryById(long id) {
		return (CityIpDO)getSqlMapClientTemplate().queryForObject("CityIpDAO.queryById", id);
	}

	@Override
	public CityIpDO queryByNumIp(long numIp) {
		return (CityIpDO)getSqlMapClientTemplate().queryForObject("CityIpDAO.queryByNumIp", numIp);
	}

	@Override
	public void update(CityIpDO cityIpDO) {
		getSqlMapClientTemplate().update("CityIpDAO.update", cityIpDO);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().delete("CityIpDAO.delete", id);
	}

}
