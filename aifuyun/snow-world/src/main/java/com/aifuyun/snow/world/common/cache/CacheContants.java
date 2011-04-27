package com.aifuyun.snow.world.common.cache;

public interface CacheContants {
	
	String DEFAULT_KEY = "default_key";
	
	/**
	 * 城市列表
	 */
	int CITY_LIST_KEY = 1;
	
	/**
	 * 热门城市
	 */
	int HOT_CITIES_KEY = 2;
	
	/**
	 * 每个城市的最新拼车
	 */
	int RECENT_CITY_TAXI_ORDERS = 3;
	
	/**
	 * 每个城市的上下班拼车
	 */
	int RECENT_CITY_SFC_ORDERS = 4;
	
	/**
	 * 每个城市的上下班拼车
	 */
	int RECENT_CITY_WORK_ORDERS = 5;
	
	/**
	 * 全局最近拼车单
	 */
	int GLOBAL_RECENT_CITY_ORDERS = 6;

}
