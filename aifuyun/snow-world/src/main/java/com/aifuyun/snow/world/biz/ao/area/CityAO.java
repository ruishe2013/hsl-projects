package com.aifuyun.snow.world.biz.ao.area;

import com.zjuh.sweet.result.Result;

public interface CityAO {
	
	/**
	 * ��ȡѡ��ĳ���
	 * @return
	 */
	Result querySelectedCity();
	
	Result queryHotCityList();
	
	Result queryAllProviceAndCities();
	
	Result switchCity(int cityId);
	
}
