package com.aifuyun.snow.world.dal.daointerface.area;

import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;

public interface CityIpDAO {
	
	/**
	 * ����һ����¼
	 * @param cityIpDO
	 * @return
	 */
	long create(CityIpDO cityIpDO);
	
	/**
	 * ͨ��id��ѯ
	 * @param id
	 * @return
	 */
	CityIpDO queryById(long id);
	
	
	/**
	 * ͨ������ip��ѯ�����䣬�൱�� numIp >= startIp and numIp <= endIp��������ж�������������޸ĵ�һ��
	 * 
	 * @param numIp
	 * @return
	 */
	CityIpDO queryByNumIp(long numIp);
	
	/**
	 * ����
	 * @param cityIpDO
	 */
	void update(CityIpDO cityIpDO);
	
	/**
	 * ɾ��
	 * @param id
	 */
	void delete(long id);
	
}
