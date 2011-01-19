package com.aifuyun.snow.world;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author pister
 * 2011-1-19
 */
public abstract class BaseTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"applicationContext.xml"};
	}
	
}
