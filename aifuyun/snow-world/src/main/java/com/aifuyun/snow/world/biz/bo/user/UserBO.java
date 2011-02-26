package com.aifuyun.snow.world.biz.bo.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;

/**
 * @author pister
 * 2011-1-22
 */
public interface UserBO {

	/**
	 * ͨ��email��ѯ
	 * @param email
	 * @return
	 */
	BaseUserDO queryByEmail(String email);

	/**
	 * ͨ��email��ѯ(����ɾ�����)
	 * @param email
	 * @return
	 */
	BaseUserDO queryByEmailIgnoreDeletedFlag(String email);
	
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
	 * ��ѯ�û�
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsername(String username);
	
	/**
	 * ����id��ѯ�û�
	 * @param id
	 * @return
	 */
	BaseUserDO queryById(long id);
	
	/**
	 * ���û��Ƿ������д�������
	 * @param usename
	 * @return
	 */
	boolean isUserInSensitivityList(String username);
	
	/**
	 * �����û�������id����
	 * @param baseUserDO
	 */
	void update(BaseUserDO baseUserDO);
	
}
