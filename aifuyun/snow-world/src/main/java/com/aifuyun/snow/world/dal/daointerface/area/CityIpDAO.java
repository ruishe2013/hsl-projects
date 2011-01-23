package com.aifuyun.snow.world.dal.daointerface.area;

import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;

public interface CityIpDAO {
	
	/**
	 * 创建一条记录
	 * @param cityIpDO
	 * @return
	 */
	long create(CityIpDO cityIpDO);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	CityIpDO queryById(long id);
	
	
	/**
	 * 通过数字ip查询（区间，相当于 numIp >= startIp and numIp <= endIp），如果有多条，返回最后修改的一条
	 * 
	 * @param numIp
	 * @return
	 */
	CityIpDO queryByNumIp(long numIp);
	
	/**
	 * 更新
	 * @param cityIpDO
	 */
	void update(CityIpDO cityIpDO);
	
	/**
	 * 删除
	 * @param id
	 */
	void delete(long id);
	
}
