package com.aifuyun.snow.world.biz.dal.dataobject.feedback;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenLeaveWordDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_");
		IbatisGenUtil.genToConsole(LeaveWordDO.class);
	}

}
