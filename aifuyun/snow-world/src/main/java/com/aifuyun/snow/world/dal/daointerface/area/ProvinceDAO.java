package com.aifuyun.snow.world.dal.daointerface.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;

public interface ProvinceDAO {
	
	/**
	 * 创建省份
	 * @param provinceDO
	 * @return
	 */
	int create(ProvinceDO provinceDO);
	
	/**
	 * 删除省份
	 * @param id
	 */
	void delete(int id);
	
	/**
	 * 更新身份
	 * @param provinceDO
	 */
	void update(ProvinceDO provinceDO);
	
	/**
	 * 通过id获取身份
	 * @param id
	 * @return
	 */
	ProvinceDO queryById(int id);
	
	/**
	 * 获取所有身份
	 * @return
	 */
	List<ProvinceDO> queryAll();

}
