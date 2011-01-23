package com.aifuyun.snow.world.biz.dal.dataobject.area;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenProvinceDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_area_");
		IbatisGenUtil.genToConsole(ProvinceDO.class);
	}

}
