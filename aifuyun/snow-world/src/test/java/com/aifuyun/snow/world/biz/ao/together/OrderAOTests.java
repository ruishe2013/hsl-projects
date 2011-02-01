package com.aifuyun.snow.world.biz.ao.together;

import java.text.ParseException;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.dataobject.together.TogetherOrderDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public class OrderAOTests extends SnowWorldTest {
	
	private OrderAO orderAO;
	
	public void testCreateTaxiOrder() throws ParseException {
		// Î´µÇÂ½µÄÇé¿ö
		this.setLogout();
		Result result = orderAO.createTaxiOrder(createNormalTogetherOrderDO());
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		setLogin(999, "test_demo_user");
		result = orderAO.createTaxiOrder(createNormalTogetherOrderDO());
		Assert.assertTrue(result.isSuccess());
		
	}
	
	private TogetherOrderDO createNormalTogetherOrderDO() throws ParseException {
		TogetherOrderDO togetherOrderDO = new TogetherOrderDO();
		togetherOrderDO.setArriveAddr("arrive addr");
		togetherOrderDO.setArriveCity("arrive city");
		togetherOrderDO.setArriveTime(DateTimeUtil.parseDate("2010-12-01 19:30:00"));
		togetherOrderDO.setDescription("this is a description");
		togetherOrderDO.setFromAddr("from addr");
		togetherOrderDO.setFromCity("from city");
		togetherOrderDO.setFromTime(DateTimeUtil.parseDate("2010-12-01 17:30:00"));
		togetherOrderDO.setTotalSeats(4);
		return togetherOrderDO;
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

}
