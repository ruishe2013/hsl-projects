package com.aifuyun.snow.world.web.common.base;

import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.Module;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

public abstract class BaseControl extends BaseModule {
	
	protected Module getCurrentScreenModule() {
		return SplistContext.getCurrentScreenModule();
	}

	public abstract void execute(RunData rundata, TemplateContext templateContext);
	
}
