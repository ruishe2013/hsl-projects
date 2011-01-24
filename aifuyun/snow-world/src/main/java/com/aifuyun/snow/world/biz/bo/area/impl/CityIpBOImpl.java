package com.aifuyun.snow.world.biz.bo.area.impl;

import com.aifuyun.snow.world.biz.bo.area.CityIpBO;
import com.aifuyun.snow.world.common.IpUtil;
import com.aifuyun.snow.world.dal.daointerface.area.CityIpDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;

public class CityIpBOImpl implements CityIpBO {

	private CityIpDAO cityIpDAO;

	public long create(CityIpDO cityIpDO) {
		return cityIpDAO.create(cityIpDO);
	}

	public CityIpDO queryById(long id) {
		return cityIpDAO.queryById(id);
	}

	public CityIpDO queryByNumIp(long numIp) {
		return cityIpDAO.queryByNumIp(numIp);
	}
	
	public CityIpDO queryByIp(String ipAddress) {
		long numIp = IpUtil.stringToNumberAddress(ipAddress);
		return cityIpDAO.queryByNumIp(numIp);
	}

	public void update(CityIpDO cityIpDO) {
		cityIpDAO.update(cityIpDO);
	}

	public void delete(long id) {
		cityIpDAO.delete(id);
	}

	public void setCityIpDAO(CityIpDAO cityIpDAO) {
		this.cityIpDAO = cityIpDAO;
	}
	
}
