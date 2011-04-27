package com.aifuyun.snow.world.web.common.base;

import java.util.List;

import com.zjuh.splist.core.form.Field;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;

/**
 * @author ally
 *
 */
public abstract class BaseScreen extends BaseModule {

	public abstract void execute(RunData rundata, TemplateContext templateContext);
	
	@SuppressWarnings("unchecked")
	protected void setFieldValue(Field field, Object value) {
		if(field == null) {
			return;
		}
		if(value instanceof List) {
			field.setValues((List<Object>)value);
		}
		field.setValue(value);
	}
	
	protected String trimZero(String time) {
		if(time == null) {
			return null;
		}
		if(time.indexOf("0") == 0) {
			return time.substring(1);
		}
		return time;
	}
	
}
