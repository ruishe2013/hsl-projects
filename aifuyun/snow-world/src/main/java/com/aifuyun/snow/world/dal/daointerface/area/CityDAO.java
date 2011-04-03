package com.aifuyun.snow.world.dal.daointerface.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.CityDO;

public interface CityDAO {

	CityDO queryByName(String name);
	
	/**
	 * ��������
	 * @param cityDO
	 * @return
	 */
	int create(CityDO cityDO);
	
	/**
	 * ���³���
	 * @param cityDO
	 */
	void update(CityDO cityDO);
	
	/**
	 * ͨ��id��ȡ
	 * @param id
	 * @return
	 */
	CityDO queryById(int id);
	
	/**
	 * ͨ��ʡ��id��ȡ
	 * @param provinceId
	 * @return
	 */
	List<CityDO> queryByProvinceId(int provinceId);
	
	List<CityDO> queryByProvinceIds(List<Integer> provinceIds);
	
	/**
	 * ɾ��
	 * @param id
	 */
	void delete(int id);
	
	List<CityDO> queryHotcities();
	
}
