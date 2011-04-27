package com.aifuyun.snow.world.biz.ao.misc.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.misc.SnowWorldAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.common.SnowUtil;
import com.aifuyun.snow.world.common.cache.CacheContants;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class SnowWorldAOImpl extends BaseAO implements SnowWorldAO {

	private OrderBO orderBO;
	
	private int defaultCityId = 1;
	
	private int ordersCount = 5;
	
	/**
	 * ����ƴ���� 10���ӻ���
	 */
	private int recentOrdersExpire = 60 * 10;
	
	/**
	 * ��ҳ��Ĭ����ʾ������ʱ��ȵ�ǰʱ��ٶ���(��λ:��) Ĭ�� 30����
	 */
	private int defaultSearchTimeDelay = 30 * 60;
	
	@Override
	public Result handleIndex() {
		Result result = new ResultSupport(false);
		try {
			CityDO city = this.getSelectedCity(defaultCityId);
			int cityId = defaultCityId;
			if (city != null) {
				cityId = city.getId();
			}
			List<OrderDO> recentTaxiOrders = getRecentTaxiOrders(cityId);
			List<OrderDO> recentSfcOrders = getRecentSfcOrders(cityId);
			List<OrderDO> recentWorkOrders = getRecentWorkOrders(cityId);
			
			Date defaultSearchDate = DateUtil.addSecond(new Date(), defaultSearchTimeDelay);
			
			int defaultSearchMinutes = SnowUtil.getRecentMinute(defaultSearchDate, 15);
			
			result.getModels().put("defaultSearchDate", defaultSearchDate);
			result.getModels().put("defaultSearchMinutes", defaultSearchMinutes);
			result.getModels().put("recentTaxiOrders", recentTaxiOrders);
			result.getModels().put("recentSfcOrders", recentSfcOrders);
			result.getModels().put("recentWorkOrders", recentWorkOrders);
			result.getModels().put("selectedCity", city);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�鿴���ƴ��ʧ��", e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<OrderDO> getRecentOrders(int cityId, int cacheArea, OrderTypeEnum orderTypeEnum) {
		List<OrderDO> recentOrders = (List<OrderDO>)this.cacheManager.get(cacheArea, cityId);
		if (recentOrders != null) {
			return adjustOrderCount(orderTypeEnum, recentOrders);
		}
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setCityId(cityId);
		orderQuery.setPageSize(ordersCount);
		orderQuery.setPageNo(1);
		orderQuery.setType(orderTypeEnum.getValue());
		recentOrders = orderBO.queryRecentTypeOrders(orderQuery);
		cacheManager.put(cacheArea, cityId, recentOrders, recentOrdersExpire);
		return adjustOrderCount(orderTypeEnum, recentOrders);
	}
	
	@SuppressWarnings("unchecked")
	private List<OrderDO> getGlobalRecentOrders(OrderTypeEnum orderTypeEnum) {
		List<OrderDO> recentOrders = (List<OrderDO>)this.cacheManager.get(CacheContants.GLOBAL_RECENT_CITY_ORDERS, orderTypeEnum.getValue());
		if (recentOrders != null) {
			return recentOrders;
		}
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setPageSize(ordersCount);
		orderQuery.setPageNo(1);
		orderQuery.setType(orderTypeEnum.getValue());
		recentOrders = orderBO.queryRecentTypeOrders(orderQuery);
		cacheManager.put(CacheContants.GLOBAL_RECENT_CITY_ORDERS,  orderTypeEnum.getValue(), recentOrders, recentOrdersExpire);
		return recentOrders;
	}
	
	private List<OrderDO> adjustOrderCount(OrderTypeEnum orderTypeEnum, List<OrderDO> inputOrders) {
		if (CollectionUtil.isEmpty(inputOrders)) {
			return getGlobalRecentOrders(orderTypeEnum);
		}
		int leftSize = ordersCount - inputOrders.size();
		if (leftSize > 0) {
			List<OrderDO> ret = new ArrayList<OrderDO>(inputOrders);
			List<OrderDO> globalOrders = getGlobalRecentOrders(orderTypeEnum);
			if (globalOrders.size() < leftSize) {
				ret.addAll(globalOrders);
			} else {
				ret.addAll(globalOrders.subList(0, leftSize));
			}
			return ret;
		} else {
			return inputOrders;
		}
	}
	
	private List<OrderDO> getRecentWorkOrders(int cityId) {
		return getRecentOrders(cityId, CacheContants.RECENT_CITY_WORK_ORDERS, OrderTypeEnum.WORK);
	}
	
	private List<OrderDO> getRecentTaxiOrders(int cityId) {
		return getRecentOrders(cityId, CacheContants.RECENT_CITY_TAXI_ORDERS, OrderTypeEnum.TAXI);
	}
	
	private List<OrderDO> getRecentSfcOrders(int cityId) {
		return getRecentOrders(cityId, CacheContants.RECENT_CITY_SFC_ORDERS, OrderTypeEnum.SFC);
	}

	public void setOrderBO(OrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public void setDefaultCityId(int defaultCityId) {
		this.defaultCityId = defaultCityId;
	}

	public int getOrdersCount() {
		return ordersCount;
	}

	public void setOrdersCount(int ordersCount) {
		this.ordersCount = ordersCount;
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
