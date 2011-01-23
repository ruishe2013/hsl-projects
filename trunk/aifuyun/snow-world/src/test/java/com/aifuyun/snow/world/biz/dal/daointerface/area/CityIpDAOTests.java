package com.aifuyun.snow.world.biz.dal.daointerface.area;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.aifuyun.snow.world.dal.daointerface.area.CityIpDAO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.test.BaseTest;

public class CityIpDAOTests extends BaseTest {
	
	private CityIpDAO cityIpDAO;

	public void testCreate() {
		final int CITY_ID = 1;
		final String CITY_NAME = "temp_city";
		final long ipStart = 1234;
		final long ipEnd = 5678;
		CityIpDO cityIpDO = new CityIpDO();
		cityIpDO.setCityId(CITY_ID);
		cityIpDO.setCityName(CITY_NAME);
		cityIpDO.setIpStart(ipStart);
		cityIpDO.setIpEnd(ipEnd);
		long id = cityIpDAO.create(cityIpDO);
		Assert.assertTrue(id > 0L);
		List<NameAndClass> nameAndClasses = CollectionUtil.newArrayList();
		nameAndClasses.add(new NameAndClass("city_id", Integer.class));
		nameAndClasses.add(new NameAndClass("city_name", String.class));
		nameAndClasses.add(new NameAndClass("ip_start", Long.class));
		nameAndClasses.add(new NameAndClass("ip_end", Long.class));
		Map<String, Object> result = this.queryFields("select city_id, city_name, ip_start, ip_end from sw_area_city_ip where id = ?", nameAndClasses, id);
		Assert.assertNotNull(result);
		Assert.assertEquals(CITY_ID, result.get("city_id"));
		Assert.assertEquals(CITY_NAME, result.get("city_name"));
		Assert.assertEquals(ipStart, result.get("ip_start"));
		Assert.assertEquals(ipEnd, result.get("ip_end"));
	}
	
	public void testQueryById() {
		long id = createCityIp();
		CityIpDO inDb = cityIpDAO.queryById(id);
		Assert.assertNotNull(inDb);
		this.executeUpdate("delete from sw_area_city_ip where id = ?", id);
		inDb = cityIpDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testDelete() {
		long id = createCityIp();
		CityIpDO inDb = cityIpDAO.queryById(id);
		Assert.assertNotNull(inDb);
		cityIpDAO.delete(id);
		inDb = cityIpDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testUpdate() {
		long id = createCityIp();
		CityIpDO inDb = cityIpDAO.queryById(id);
		Assert.assertNotNull(inDb);
		final int CITY_ID = 2;
		final String CITY_NAME = "temp_city_new";
		final long ipStart = 12346;
		final long ipEnd = 56787;
		inDb.setCityId(CITY_ID);
		inDb.setCityName(CITY_NAME);
		inDb.setIpStart(ipStart);
		inDb.setIpEnd(ipEnd);
		cityIpDAO.update(inDb);
		inDb = cityIpDAO.queryById(id);
		Assert.assertNotNull(inDb);
		
		Assert.assertEquals(CITY_ID, inDb.getCityId());
		Assert.assertEquals(CITY_NAME, inDb.getCityName());
		Assert.assertEquals(ipStart, inDb.getIpStart());
		Assert.assertEquals(ipEnd, inDb.getIpEnd());
	}
	
	public void testQueryByNumIp() {
		final int CITY_ID = 1;
		final String CITY_NAME = "temp_city";
		final long ipStart = 1234;
		final long ipEnd = 5678;
		CityIpDO cityIpDO = new CityIpDO();
		cityIpDO.setCityId(CITY_ID);
		cityIpDO.setCityName(CITY_NAME);
		cityIpDO.setIpStart(ipStart);
		cityIpDO.setIpEnd(ipEnd);
		long id = cityIpDAO.create(cityIpDO);
		Assert.assertTrue(id > 0L);
		CityIpDO inDb = cityIpDAO.queryByNumIp(1234);
		Assert.assertNotNull(inDb);
		inDb = cityIpDAO.queryByNumIp(1235);
		Assert.assertNotNull(inDb);
		inDb = cityIpDAO.queryByNumIp(1233);
		if (inDb != null) {
			Assert.assertNotSame(CITY_ID, inDb.getCityId());
		}
		
		inDb = cityIpDAO.queryByNumIp(5677);
		Assert.assertNotNull(inDb);
		inDb = cityIpDAO.queryByNumIp(5678);
		Assert.assertNotNull(inDb);
		inDb = cityIpDAO.queryByNumIp(5679);
		if (inDb != null) {
			Assert.assertNotSame(CITY_ID, inDb.getCityId());
		}
	}
	
	private long createCityIp() {
		final int CITY_ID = 1;
		final String CITY_NAME = "temp_city";
		final long ipStart = 1234;
		final long ipEnd = 5678;
		CityIpDO cityIpDO = new CityIpDO();
		cityIpDO.setCityId(CITY_ID);
		cityIpDO.setCityName(CITY_NAME);
		cityIpDO.setIpStart(ipStart);
		cityIpDO.setIpEnd(ipEnd);
		long id = cityIpDAO.create(cityIpDO);
		Assert.assertTrue(id > 0L);
		return id;
	}
	
	public void setCityIpDAO(CityIpDAO cityIpDAO) {
		this.cityIpDAO = cityIpDAO;
	}

}
