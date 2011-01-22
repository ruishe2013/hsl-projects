package com.aifuyun.snow.world.dal.daointerface.user;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;

/**
 * @author pister
 * 2011-1-19
 */
public interface BaseUserDAO {
	
	/**
	 * �����û�
	 * @param baseUserDO
	 * @return
	 */
	long create(BaseUserDO baseUserDO);
	
	/**
	 * ɾ���û�����δ�߼�ɾ��
	 * @param id
	 */
	void delete(long id);
	
	/**
	 * ͨ��id�����û��������ֶ�
	 * @param baseUserDO
	 */
	void update(BaseUserDO baseUserDO);
	
	/**
	 * ͨ��id��ѯ
	 * @param id
	 * @return
	 */
	BaseUserDO queryById(long id);
	
	/**
	 * ͨ��id��ѯ������ɾ����ǣ�Ҳ�����ܲ���������е�����
	 * @param nick
	 * @return
	 */
	BaseUserDO queryByIdIgnoreDeletedFlag(long id);
	
	/**
	 * ͨ�����id��ѯ��ֻ���ز�ѯ���Ľ��
	 * @param ids
	 * @return
	 */
	List<BaseUserDO> queryByIds(List<Long> ids);
	
	/**
	 * ͨ��username��ѯ
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsername(String username);
	
	/**
	 * ͨ��username��ѯ������ɾ����ǣ�Ҳ�����ܲ���������е�����
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsernameIgnoreDeletedFlag(String username);

}
