package com.aifuyun.snow.world.biz.dal.daointerface.area;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.aifuyun.snow.world.dal.daointerface.area.CityDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.test.BaseTest;

public class CityDAOTests extends BaseTest {

	private CityDAO cityDAO;

	public void testCreate() {
		final String NAME = "temp_city_name";
		final String PINYIN = "temp_city_pinyin";
		final int provinceId = 1;
		CityDO cityDO = new CityDO();
		cityDO.setName(NAME);
		cityDO.setPinyin(PINYIN);
		cityDO.setProvinceId(provinceId);
		int id = cityDAO.create(cityDO);
		Assert.assertTrue(id > 0);
		List<NameAndClass> nameAndClasses = CollectionUtil.newArrayList();
		nameAndClasses.add(new NameAndClass("name", String.class));
		nameAndClasses.add(new NameAndClass("pinyin", String.class));
		nameAndClasses.add(new NameAndClass("province_id", Integer.class));
		Map<String, Object> result = queryFields("select name, pinyin, province_id from sw_area_city where id = ?", nameAndClasses, id);
		Assert.assertNotNull(result);
		Assert.assertEquals(NAME, result.get("name"));
		Assert.assertEquals(PINYIN, result.get("pinyin"));
		Assert.assertEquals(provinceId, result.get("province_id"));
	}
	
	public void testQueryById() {
		final String NAME = "temp_city_name";
		final String PINYIN = "temp_city_pinyin";
		final int provinceId = 1;
		int id = createCity(NAME, PINYIN, provinceId);
		CityDO inDb = cityDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		Assert.assertEquals(PINYIN, inDb.getPinyin());
		Assert.assertEquals(provinceId, inDb.getProvinceId());
		
		this.executeUpdate("delete from sw_area_city where id = ?", id);
		inDb = cityDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testDelete() {
		final String NAME = "temp_city_name";
		final String PINYIN = "temp_city_pinyin";
		final int provinceId = 1;
		int id = createCity(NAME, PINYIN, provinceId);
		CityDO inDb = cityDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		Assert.assertEquals(PINYIN, inDb.getPinyin());
		Assert.assertEquals(provinceId, inDb.getProvinceId());
		
		cityDAO.delete(id);
		inDb = cityDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testUpdate() {
		final String NAME = "temp_city_name";
		final String PINYIN = "temp_city_pinyin";
		final int provinceId = 1;
		int id = createCity(NAME, PINYIN, provinceId);
		CityDO inDb = cityDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME, inDb.getName());
		Assert.assertEquals(PINYIN, inDb.getPinyin());
		Assert.assertEquals(provinceId, inDb.getProvinceId());
		
		final String NAME_NEW = "temp_city_name_new";
		final String PINYIN_NEW = "temp_city_pinyin_new";
		final int provinceIdNew = 2;
		inDb.setName(NAME_NEW);
		inDb.setPinyin(PINYIN_NEW);
		inDb.setProvinceId(provinceIdNew);
		cityDAO.update(inDb);
		
		inDb = cityDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(NAME_NEW, inDb.getName());
		Assert.assertEquals(PINYIN_NEW, inDb.getPinyin());
		Assert.assertEquals(provinceIdNew, inDb.getProvinceId());
	}
	
	public void testQueryByProvinceId() {
		final int provinceId = 3;
		final int count = 4;
		for (int i = 0; i < count; ++i) {
			createCity("temp_name_" + i, "temp_pinyin_" + i, provinceId);
		}
		List<CityDO> cities = cityDAO.queryByProvinceId(provinceId);
		Assert.assertNotNull(cities);
		Assert.assertTrue(cities.size() >= count);
	}
	
	private int createCity(String name, String pinyin, int provinceId) {
		CityDO cityDO = new CityDO();
		cityDO.setName(name);
		cityDO.setPinyin(pinyin);
		cityDO.setProvinceId(provinceId);
		int id = cityDAO.create(cityDO);
		Assert.assertTrue(id > 0);
		return id;
	}
	
	public void setCityDAO(CityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}
	
}
