package com.aifuyun.snow.world.biz.ao.area;

import com.zjuh.sweet.result.Result;

public interface CityAO {
	
	/**
	 * ��ȡѡ��ĳ���
	 * @param ipAddress
	 * @param cityIdFromCookie 
	 * @return
	 */
	Result querySelectedCity(String ipAddress, String cityIdFromCookie);
	
	Result queryAllProviceAndCities();

}
