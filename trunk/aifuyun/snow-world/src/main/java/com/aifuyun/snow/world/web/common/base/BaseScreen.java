package com.aifuyun.snow.world.web.common.base;

import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

/**
 * @author ally
 *
 */
public abstract class BaseScreen extends BaseModule {

	public abstract void execute(RunData rundata, TemplateContext templateContext);
	
}
