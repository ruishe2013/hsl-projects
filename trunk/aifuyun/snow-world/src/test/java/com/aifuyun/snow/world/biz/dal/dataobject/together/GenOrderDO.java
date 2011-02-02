package com.aifuyun.snow.world.biz.dal.dataobject.together;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenOrderDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_");
		IbatisGenUtil.genToConsole(OrderDO.class);
	}

}
