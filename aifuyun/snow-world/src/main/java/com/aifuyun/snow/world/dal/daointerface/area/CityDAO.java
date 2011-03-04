package com.aifuyun.snow.world.dal.daointerface.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.CityDO;

public interface CityDAO {

	/**
	 * 创建城市
	 * @param cityDO
	 * @return
	 */
	int create(CityDO cityDO);
	
	/**
	 * 更新城市
	 * @param cityDO
	 */
	void update(CityDO cityDO);
	
	/**
	 * 通过id获取
	 * @param id
	 * @return
	 */
	CityDO queryById(int id);
	
	/**
	 * 通过省份id获取
	 * @param provinceId
	 * @return
	 */
	List<CityDO> queryByProvinceId(int provinceId);
	
	List<CityDO> queryByProvinceIds(List<Integer> provinceIds);
	
	/**
	 * 删除
	 * @param id
	 */
	void delete(int id);
	
}
