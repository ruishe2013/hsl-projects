package com.aifuyun.snow.world.biz.bo.area.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.area.CityBO;
import com.aifuyun.snow.world.dal.daointerface.area.CityDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;

public class CityBOImpl implements CityBO {
	
	private CityDAO cityDAO;
	
	public int create(CityDO cityDO) {
		return cityDAO.create(cityDO);
	}

	public void update(CityDO cityDO) {
		cityDAO.update(cityDO);
	}
	
	public CityDO queryById(int id) {
		return cityDAO.queryById(id);
	}

	@Override
	public List<CityDO> queryByProvinceIds(List<Integer> provinceIds) {
		return cityDAO.queryByProvinceIds(provinceIds);
	}

	public List<CityDO> queryByProvinceId(int provinceId) {
		return cityDAO.queryByProvinceId(provinceId);
	}

	public void delete(int id) {
		cityDAO.delete(id);
	}

	public void setCityDAO(CityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}

}
