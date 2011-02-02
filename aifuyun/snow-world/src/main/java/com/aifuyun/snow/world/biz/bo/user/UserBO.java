package com.aifuyun.snow.world.biz.bo.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;

/**
 * @author pister
 * 2011-1-22
 */
public interface UserBO {

	/**
	 * 加密密码
	 * @param password
	 * @return
	 */
	String encryptPassword(String password);
	
	/**
	 * 创建用户
	 * @param userDO
	 * @return
	 */
	long create(BaseUserDO userDO);
	
	/**
	 * 查询用户，忽略deleted标记
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsernameIgnoreDeletedFlag(String username);
	
	/**
	 * 查询用户
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsername(String username);
	
	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 */
	BaseUserDO queryById(long id);
	
	/**
	 * 该用户是否在敏感词名单里
	 * @param usename
	 * @return
	 */
	boolean isUserInSensitivityList(String username);
	
	/**
	 * 更新用户，根据id更新
	 * @param baseUserDO
	 */
	void update(BaseUserDO baseUserDO);
	
}
