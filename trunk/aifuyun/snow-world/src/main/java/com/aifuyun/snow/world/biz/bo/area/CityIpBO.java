package com.aifuyun.snow.world.biz.bo.area;

import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;

public interface CityIpBO {

	public long create(CityIpDO cityIpDO);

	public CityIpDO queryById(long id);

	public CityIpDO queryByIp(String ipAddress);

	public void update(CityIpDO cityIpDO);

	public void delete(long id);
	
}
