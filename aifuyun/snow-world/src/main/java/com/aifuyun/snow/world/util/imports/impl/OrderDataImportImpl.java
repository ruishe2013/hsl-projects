package com.aifuyun.snow.world.util.imports.impl;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.util.imports.OrderDataImport;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultCode;

public class OrderDataImportImpl implements OrderDataImport {
	
	private OrderAO orderAO;

	public ResultCode importOrderData(OrderDO orderDO, OrderUserDO inputCreator) {
		Result result = orderAO.createOrder(orderDO);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		long orderId = (Long)result.getModels().get("orderId");
		result = orderAO.fillCreatorInfo(inputCreator, orderId, false);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		orderAO.confirmFinishOrder(orderId);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		return null;
	}
	
	
	
	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
