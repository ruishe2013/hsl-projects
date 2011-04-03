package com.aifuyun.snow.world.biz.ao.misc.impl;

import java.util.Date;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.misc.SnowWorldAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.common.SnowUtil;
import com.aifuyun.snow.world.common.cache.CacheContants;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class SnowWorldAOImpl extends BaseAO implements SnowWorldAO {

	private OrderBO orderBO;
	
	private int defaultCityId = 1;
	
	private int cityCount = 10;
	
	/**
	 * 最新拼车， 30分钟缓存
	 */
	private int recentOrdersExpire = 60 * 30;
	
	/**
	 * 首页上默认显示的搜索时间比当前时间迟多少(单位:秒) 默认 30分钟
	 */
	private int defaultSearchTimeDelay = 30 * 60;
	
	@Override
	public Result handleForTaxiIndex() {
		Result result = new ResultSupport(false);
		try {
			CityDO city = this.getSelectedCity(defaultCityId);
			int cityId = defaultCityId;
			if (city != null) {
				cityId = city.getId();
			}
			List<OrderDO> recentOrders = getRecentOrders(cityId);
			Date defaultSearchDate = DateUtil.addSecond(new Date(), defaultSearchTimeDelay);
			
			int defaultSearchMinutes = SnowUtil.getRecentMinute(defaultSearchDate, 15);
			
			result.getModels().put("defaultSearchDate", defaultSearchDate);
			result.getModels().put("defaultSearchMinutes", defaultSearchMinutes);
			result.getModels().put("recentOrders", recentOrders);
			result.getModels().put("selectedCity", city);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("查看最近拼车失败", e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<OrderDO> getRecentOrders(int cityId) {
		List<OrderDO> recentOrders = (List<OrderDO>)this.cacheManager.get(CacheContants.RECENT_CITY_ORDERS, cityId);
		if (recentOrders != null) {
			return recentOrders;
		}
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setCityId(cityId);
		orderQuery.setPageSize(cityCount);
		orderQuery.setPageNo(1);
		recentOrders = orderBO.queryRecentOrders(orderQuery);
		cacheManager.put(CacheContants.RECENT_CITY_ORDERS, cityId, recentOrders, recentOrdersExpire);
		return recentOrders;
	}

	public void setOrderBO(OrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public void setDefaultCityId(int defaultCityId) {
		this.defaultCityId = defaultCityId;
	}

	public void setCityCount(int cityCount) {
		this.cityCount = cityCount;
	}

	public void setRecentOrdersExpire(int recentOrdersExpire) {
		this.recentOrdersExpire = recentOrdersExpire;
	}

	public void setDefaultSearchTimeDelay(int defaultSearchTimeDelay) {
		if (defaultSearchTimeDelay < 0) {
			defaultSearchTimeDelay = 0;
		}
		this.defaultSearchTimeDelay = defaultSearchTimeDelay;
	}

}
