package com.aifuyun.snow.world.biz.ao.area;

import com.zjuh.sweet.result.Result;

public interface CityAO {
	
	/**
	 * 获取选择的城市
	 * @param ipAddress
	 * @return
	 */
	Result querySelectedCity(String ipAddress);
	
	Result queryAllProviceAndCities();
	
	Result switchCity(int cityId);

}
