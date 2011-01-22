package com.aifuyun.snow.world.biz.bo.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;

/**
 * @author pister
 * 2011-1-22
 */
public interface UserBO {

	/**
	 * ��������
	 * @param password
	 * @return
	 */
	String encryptPassword(String password);
	
	/**
	 * �����û�
	 * @param userDO
	 * @return
	 */
	long create(BaseUserDO userDO);
	
	/**
	 * ��ѯ�û�������deleted���
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsernameIgnoreDeletedFlag(String username);
	
	/**
	 * ���û��Ƿ������д�������
	 * @param usename
	 * @return
	 */
	boolean isUserInSensitivityList(String username);
	
}
