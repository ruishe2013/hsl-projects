package com.aifuyun.snow.world.biz.bo.area.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.area.ProvinceBO;
import com.aifuyun.snow.world.dal.daointerface.area.ProvinceDAO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;

public class ProvinceBOImpl implements ProvinceBO {
	
	private ProvinceDAO provinceDAO;

	public int create(ProvinceDO provinceDO) {
		return provinceDAO.create(provinceDO);
	}

	public void delete(int id) {
		provinceDAO.delete(id);
	}

	public void update(ProvinceDO provinceDO) {
		provinceDAO.update(provinceDO);
	}

	public ProvinceDO queryById(int id) {
		return provinceDAO.queryById(id);
	}

	public List<ProvinceDO> queryAll() {
		return provinceDAO.queryAll();
	}

	public void setProvinceDAO(ProvinceDAO provinceDAO) {
		this.provinceDAO = provinceDAO;
	}

}
