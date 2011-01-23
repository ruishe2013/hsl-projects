package com.aifuyun.snow.world.biz.dal.daointerface.area;

import java.util.List;

import junit.framework.Assert;

import com.aifuyun.snow.world.dal.daointerface.area.ProvinceDAO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;
import com.zjuh.sweet.test.BaseTest;

public class ProvinceDAOTests extends BaseTest {
	
	private ProvinceDAO provinceDAO;

	public void testCreate() {
		final String NAME = "temp_test_city";
		final String PINYIN = "test_py";
		ProvinceDO provinceDO = new ProvinceDO();
		provinceDO.setName(NAME);
		provinceDO.setPinyin(PINYIN);
		int id = provinceDAO.create(provinceDO);
		Assert.assertTrue(id > 0);
		String name = this.queryField("select name from sw_area_province where id = ?", "name", String.class, id);
		Assert.assertEquals(NAME, name);
		String pinyin = this.queryField("select pinyin from sw_area_province where id = ?", "pinyin", String.class, id);
		Assert.assertEquals(PINYIN, pinyin);
	}
	
	public void testQueryById() {
		final String NAME = "temp_test_city";
		final String PINYIN = "test_py";
		int id =  createProvince(NAME, PINYIN);
		ProvinceDO inDb = provinceDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		this.executeUpdate("delete from sw_area_province where id = ?", id);
		inDb = provinceDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testDelete() {
		final String NAME = "temp_test_city";
		final String PINYIN = "test_py";
		int id =  createProvince(NAME, PINYIN);
		ProvinceDO inDb = provinceDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		provinceDAO.delete(id);
		inDb = provinceDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testUpdate() {
		final String NAME = "temp_test_city";
		final String PINYIN = "test_py";
		final String NEW_NAME = "temp_test_city_new";
		int id = createProvince(NAME, PINYIN);
		ProvinceDO inDb = provinceDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		inDb.setName(NEW_NAME);
		provinceDAO.update(inDb);
		inDb = provinceDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NEW_NAME, inDb.getName());
	}
	
	public void testQueryAll() {
		final int count = 3;
		for (int i = 0; i < count; ++i) {
			createProvince("temp_first_name", "temp_first_pinyin");
		}
		List<ProvinceDO> inDb =  provinceDAO.queryAll();
		Assert.assertTrue(inDb != null);
		Assert.assertTrue(inDb.size() >= count);
	}
	
	private int createProvince(String name, String pinyin) {
		ProvinceDO provinceDO = new ProvinceDO();
		provinceDO.setName(name);
		provinceDO.setPinyin(pinyin);
		int id = provinceDAO.create(provinceDO);
		Assert.assertTrue(id > 0);
		return id;
	}
	
	public void setProvinceDAO(ProvinceDAO provinceDAO) {
		this.provinceDAO = provinceDAO;
	}

}
