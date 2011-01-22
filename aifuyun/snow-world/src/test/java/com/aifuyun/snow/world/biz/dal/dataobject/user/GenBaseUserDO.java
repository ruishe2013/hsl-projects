package com.aifuyun.snow.world.biz.dal.dataobject.user;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenBaseUserDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_");
		IbatisGenUtil.genToConsole(BaseUserDO.class);
	}

}
