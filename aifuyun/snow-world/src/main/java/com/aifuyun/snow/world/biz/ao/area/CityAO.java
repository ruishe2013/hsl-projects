package com.aifuyun.snow.world.biz.ao.area;

import com.zjuh.sweet.result.Result;

public interface CityAO {
	
	/**
	 * ��ȡѡ��ĳ���
	 * @param ipAddress
	 * @return
	 */
	Result querySelectedCity(String ipAddress);
	
	Result queryAllProviceAndCities();
	
	Result switchCity(int cityId);

}
