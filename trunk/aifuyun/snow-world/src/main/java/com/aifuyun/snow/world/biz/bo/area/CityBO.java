package com.aifuyun.snow.world.biz.bo.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.CityDO;

public interface CityBO {

	public int create(CityDO cityDO);

	public void update(CityDO cityDO);
	
	public CityDO queryById(int id);

	public List<CityDO> queryByProvinceId(int provinceId);
	
	public List<CityDO> queryByProvinceIds(List<Integer> provinceIds);

	public void delete(int id);
	
	public List<CityDO> queryHotcities();
	
}
