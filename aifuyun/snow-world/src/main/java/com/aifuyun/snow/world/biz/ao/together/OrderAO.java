package com.aifuyun.snow.world.biz.ao.together;

import com.aifuyun.snow.world.dal.dataobject.together.TogetherOrderDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public interface OrderAO {

	/**
	 * �������⳵ƴ��
	 * @param togetherOrderDO
	 * @return
	 */
	Result createTaxiOrder(TogetherOrderDO togetherOrderDO);
	
}
