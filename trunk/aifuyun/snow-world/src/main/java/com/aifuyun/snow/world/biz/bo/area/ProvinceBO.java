package com.aifuyun.snow.world.biz.bo.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;

public interface ProvinceBO {
	
	int create(ProvinceDO provinceDO);

	void delete(int id);

	void update(ProvinceDO provinceDO);

	ProvinceDO queryById(int id);

	List<ProvinceDO> queryAll();

}
