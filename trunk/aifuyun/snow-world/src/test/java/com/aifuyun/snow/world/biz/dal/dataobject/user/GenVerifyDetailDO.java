package com.aifuyun.snow.world.biz.dal.dataobject.user;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenVerifyDetailDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_");
		IbatisGenUtil.genToConsole(VerifyDetailDO.class);
	}

}
