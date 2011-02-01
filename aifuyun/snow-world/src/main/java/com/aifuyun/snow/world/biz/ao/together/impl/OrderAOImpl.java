package com.aifuyun.snow.world.biz.ao.together.impl;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.dal.dataobject.enums.TogetherTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.together.TogetherOrderDO;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

/**
 * @author pister
 *
 */
public class OrderAOImpl extends BaseAO implements OrderAO {

	@Override
	public Result createTaxiOrder(TogetherOrderDO togetherOrderDO) {
		Result result = new ResultSupport(false);
		try {
			final long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			final String username = this.getLoginUsername();
			togetherOrderDO.setCreatorId(userId);
			togetherOrderDO.setCreatorUsername(username);
			togetherOrderDO.setType(TogetherTypeEnum.TAXI.getValue());
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("´´½¨Æ´µÄÊ§°Ü", e);
		}
		return result;
	}

}
