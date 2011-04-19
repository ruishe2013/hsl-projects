package com.aifuyun.snow.world.web.common.validators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aifuyun.snow.world.dal.dataobject.enums.CarOwnerTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.zjuh.splist.SplistException;
import com.zjuh.splist.core.form.Field;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.core.form.validator.Validator;
import com.zjuh.sweet.lang.StringUtil;

public class CreateOrderCarNoValidator implements Validator {

	private static Set<String> validatorOrderTypes = new HashSet<String>();
	
	private static Set<String> validatorCreatorCarOwnerTypes = new HashSet<String>();
	
	static {
		validatorOrderTypes.add(String.valueOf(OrderTypeEnum.SFC.getValue()));
		validatorOrderTypes.add(String.valueOf(OrderTypeEnum.WORK.getValue()));
		
		validatorCreatorCarOwnerTypes.add(String.valueOf(CarOwnerTypeEnum.CAR_OWNER.getValue()));
	}
	
	@Override
	public void init() {
	}

	public boolean validate(Form form, String name, String value) {
		return check(form, name, value);
	}

	protected boolean check(Form form, String name, String value) {
		Map<String, Field> fields = form.getFields();
		
		// 拼车类型
		Field orderTypeField = fields.get("type");
		if (orderTypeField == null) {
			throw new SplistException("field 'type' not exist!");
		}
		String orderType = StringUtil.toString(orderTypeField.getValue());
		if (!validatorOrderTypes.contains(orderType)) {
			// 不用检测
			return true;
		}
		
		Field creatorCarOwnerTypeField = fields.get("creatorCarOwnerType");
		if (creatorCarOwnerTypeField == null) {
			throw new SplistException("field 'creatorCarOwnerType' not exist!");
		}
		String creatorCarOwnerTypeValue = StringUtil.toString(creatorCarOwnerTypeField.getValue());
		if (!validatorCreatorCarOwnerTypes.contains(creatorCarOwnerTypeValue)) {
			// 不用检测
			return true;
		}
		
		if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
			return false;
		}
		return true;
	}

}
