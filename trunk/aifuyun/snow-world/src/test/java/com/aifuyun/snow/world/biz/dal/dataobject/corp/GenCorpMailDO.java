package com.aifuyun.snow.world.biz.dal.dataobject.corp;

import junit.framework.TestCase;

import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;
import com.zjuh.sweet.tool.ibatis.IbatisGenUtil;

public class GenCorpMailDO extends TestCase {
	
	public void testGen() {
		IbatisGenUtil.setTablePrefix("sw_");
		IbatisGenUtil.genToConsole(CorpMailDO.class);
	}

}
