package com.aifuyun.snow.world.biz.dal.daointerface.user;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.test.BaseTest;

public class BaseUserTests extends BaseTest {

	private BaseUserDAO baseUserDAO;

	public void testCreate() {
		String testNick = "$$temp_test_user_name";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryById(id);
		Assert.assertNotNull(objectInDb);
	}
	
	public void testDelete() {
		String testNick = "$$temp_test_delete";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryById(id);
		Assert.assertNotNull(objectInDb);
		baseUserDAO.delete(id);
		objectInDb = baseUserDAO.queryById(id);
		Assert.assertNull(objectInDb);
	}
	
	public void testUpdate() {
		String testNick = "$$temp_test_update";
		String testpassword = "abcdedfg";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword(testpassword);
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryById(id);
		Assert.assertNotNull(objectInDb);
		Assert.assertEquals(testpassword, objectInDb.getPassword());
		String newPassword = "123455667";
		objectInDb.setPassword(newPassword);
		baseUserDAO.update(objectInDb);
		objectInDb = baseUserDAO.queryById(id);
		Assert.assertEquals(newPassword, objectInDb.getPassword());
	}
	
	public void testQueryById() {
		String testNick = "$$temp_test_query_by_id";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryById(id);
		Assert.assertNotNull(objectInDb);
		
		objectInDb = baseUserDAO.queryById(-1L);
		Assert.assertNull(objectInDb);
	}
	
	public void testQueryByIds() {
		String testNick = "$$temp_test_query_by_id";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id1 = baseUserDAO.create(baseUserDO);
		
		String testNick2 = "$$temp_test_query_by_id22";
		BaseUserDO baseUserDO2 = new BaseUserDO();
		baseUserDO2.setUsername(testNick2);
		baseUserDO2.setPassword("testpassword2");
		long id2 = baseUserDAO.create(baseUserDO);
		
		List<Long> ids1 = Arrays.asList(new Long[] {id1, id2});
		List<BaseUserDO> inDb = baseUserDAO.queryByIds(ids1);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(2, inDb.size());
		
		baseUserDAO.delete(id2);
		
		inDb = baseUserDAO.queryByIds(ids1);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(1, inDb.size());
		
	}
	
	public void testQueryByNick() {
		String testNick = "$$temp_test_query_by_nick";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryByUsername(testNick);
		Assert.assertNotNull(objectInDb);
		
		baseUserDAO.delete(id);
		objectInDb = baseUserDAO.queryByUsername(testNick);
		Assert.assertNull(objectInDb);
	}
	
	public void testQueryByNickIgnoreDeletedFlag() {
		String testNick = "$$temp_test_query_by_nick";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryByUsername(testNick);
		Assert.assertNotNull(objectInDb);
		
		baseUserDAO.delete(id);
		objectInDb = baseUserDAO.queryByUsernameIgnoreDeletedFlag(testNick);
		Assert.assertNotNull(objectInDb);
	}
	
	public void testQueryByIdIgnoreDeletedFlag() {
		String testNick = "$$temp_test_query_by_nick";
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(testNick);
		baseUserDO.setPassword("testpassword");
		long id = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(id > 0L);
		BaseUserDO objectInDb = baseUserDAO.queryById(id);
		Assert.assertNotNull(objectInDb);
		
		baseUserDAO.delete(id);
		objectInDb = baseUserDAO.queryByIdIgnoreDeletedFlag(id);
		Assert.assertNotNull(objectInDb);
	}
	
	public void setBaseUserDAO(BaseUserDAO baseUserDAO) {
		this.baseUserDAO = baseUserDAO;
	}
	
}
