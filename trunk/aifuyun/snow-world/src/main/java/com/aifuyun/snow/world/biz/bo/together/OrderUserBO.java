package com.aifuyun.snow.world.biz.bo.together;

import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public interface OrderUserBO {
	
	OrderUserDO queryOrderCreator(long orderId);

}
