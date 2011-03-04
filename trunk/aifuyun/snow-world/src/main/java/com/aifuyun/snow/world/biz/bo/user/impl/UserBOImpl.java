package com.aifuyun.snow.world.biz.bo.user.impl;

import java.util.Set;

import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.daointerface.user.ExtUserDAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.zjuh.sweet.codec.MD5;
import com.zjuh.sweet.lang.CollectionUtil;

public class UserBOImpl implements UserBO {

	private BaseUserDAO baseUserDAO;
	
	private ExtUserDAO extUserDAO;
	
	private Set<String> sensitivitiesList = CollectionUtil.newHashSet();
	
	public void createExtUser(ExtUserDO extUserDO) {
		extUserDAO.create(extUserDO);
	}
	
	public ExtUserDO queryExtUser(long userId) {
		return extUserDAO.queryByUserId(userId);
	}
	
	
	public void updateExtUser(ExtUserDO extUserDO) {
		extUserDAO.update(extUserDO);
	}
	
	public void delete(long id) {
		baseUserDAO.delete(id);
		extUserDAO.delete(id);
	}
	
	@Override
	public void update(BaseUserDO baseUserDO) {
		baseUserDAO.update(baseUserDO);
	}

	@Override
	public BaseUserDO queryById(long id) {
		return baseUserDAO.queryById(id);
	}

	@Override
	public String encryptPassword(String password) {
		return MD5.encrypt(password);
	}

	@Override
	public long create(BaseUserDO baseUserDO) {
		return baseUserDAO.create(baseUserDO);
	}

	@Override
	public BaseUserDO queryByUsernameIgnoreDeletedFlag(String username) {
		return baseUserDAO.queryByUsernameIgnoreDeletedFlag(username);
	}

	@Override
	public BaseUserDO queryByUsername(String username) {
		return baseUserDAO.queryByUsername(username);
	}

	@Override
	public boolean isUserInSensitivityList(String username) {
		return sensitivitiesList.contains(username);
	}

	public BaseUserDO queryByEmail(String email) {
		return baseUserDAO.queryByEmail(email);
	}

	public BaseUserDO queryByEmailIgnoreDeletedFlag(String email) {
		return baseUserDAO.queryByEmailIgnoreDeletedFlag(email);
	}

	public void setBaseUserDAO(BaseUserDAO baseUserDAO) {
		this.baseUserDAO = baseUserDAO;
	}

	public Set<String> getSensitivitiesList() {
		return sensitivitiesList;
	}

	public void setSensitivitiesList(Set<String> sensitivitiesList) {
		this.sensitivitiesList = sensitivitiesList;
	}

	public ExtUserDAO getExtUserDAO() {
		return extUserDAO;
	}

	public void setExtUserDAO(ExtUserDAO extUserDAO) {
		this.extUserDAO = extUserDAO;
	}

}
