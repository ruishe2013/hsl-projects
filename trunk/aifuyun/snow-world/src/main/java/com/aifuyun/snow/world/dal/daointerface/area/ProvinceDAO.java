package com.aifuyun.snow.world.dal.daointerface.area;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;

public interface ProvinceDAO {
	
	/**
	 * ����ʡ��
	 * @param provinceDO
	 * @return
	 */
	int create(ProvinceDO provinceDO);
	
	/**
	 * ɾ��ʡ��
	 * @param id
	 */
	void delete(int id);
	
	/**
	 * �������
	 * @param provinceDO
	 */
	void update(ProvinceDO provinceDO);
	
	/**
	 * ͨ��id��ȡ���
	 * @param id
	 * @return
	 */
	ProvinceDO queryById(int id);
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	List<ProvinceDO> queryAll();

}
