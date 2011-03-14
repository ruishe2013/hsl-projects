package com.aifuyun.snow.world.biz.ao.misc.impl;

import java.util.Date;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.misc.SnowWorldAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
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
	 * ����ƴ���� 3���ӻ���
	 */
	private int recentOrdersExpire = 60 * 3;
	
	/**
	 * ��ҳ��Ĭ����ʾ������ʱ��ȵ�ǰʱ��ٶ���(��λ:��) Ĭ�� 30����
	 */
	private int defaultSearchTimeDelay = 30 * 60;
	
	@Override
	public Result handleForIndex() {
		Result result = new ResultSupport(false);
		try {
			CityDO city = this.getSelectedCity(defaultCityId);
			int cityId = defaultCityId;
			if (city != null) {
				cityId = city.getId();
			}
			List<OrderDO> recentOrders = getRecentOrders(cityId);
			Date defaultSearchDate = DateUtil.addSecond(new Date(), defaultSearchTimeDelay);
			int defaultSearchMinutes = getQuarterMinute(defaultSearchDate);
			result.getModels().put("defaultSearchDate", defaultSearchDate);
			result.getModels().put("defaultSearchMinutes", defaultSearchMinutes);
			result.getModels().put("recentOrders", recentOrders);
			result.getModels().put("selectedCity", city);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�鿴���ƴ��ʧ��", e);
		}
		return result;
	}
	
	private int getQuarterMinute(Date defaultSearchDate) {
		int minute = DateUtil.getMinute(defaultSearchDate);
		return (minute / 15 ) * 15;
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
