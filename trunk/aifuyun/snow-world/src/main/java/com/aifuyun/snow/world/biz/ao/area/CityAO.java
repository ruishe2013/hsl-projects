package com.aifuyun.snow.world.biz.ao.area;

import com.zjuh.sweet.result.Result;

public interface CityAO {
	
	/**
	 * 获取选择的城市
	 * @return
	 */
	Result querySelectedCity();
	
	Result queryAllProviceAndCities();
	
	Result switchCity(int cityId);
	
}
