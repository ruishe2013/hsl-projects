package com.aifuyun.snow.world.web.common.validators;

import java.util.Map;

import com.aifuyun.snow.world.dal.dataobject.enums.CarOwnerTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.zjuh.splist.SplistException;
import com.zjuh.splist.core.form.Field;
import com.zjuh.splist.core.form.Form;
import com.zjuh.splist.core.form.validator.Validator;
import com.zjuh.sweet.lang.ConvertUtil;
import com.zjuh.sweet.lang.StringUtil;

public class CreateOrderSeatsValidator implements Validator {

	@Override
	public void init() {

	}

	@Override
	public boolean validate(Form form, String name, String value) {
		return check(form, name, value);
	}
	
	protected boolean check(Form form, String name, String value) {
		Map<String, Field> fields = form.getFields();
		Field creatorCarOwnerTypeField = fields.get("creatorCarOwnerType");
		
		// 拼车类型
		Field orderTypeField = fields.get("type");
		if (orderTypeField == null) {
			throw new SplistException("field 'type' not exist!");
		}
		String orderType = StringUtil.toString(orderTypeField.getValue());
		
		int orderTypeInt = ConvertUtil.toInt(orderType, 0);
		
		if (orderTypeInt == OrderTypeEnum.TAXI.getValue()) {
			// 如果是出租车，不能为空
			if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
				return false;
			}
			return true;
		} else if (orderTypeInt == OrderTypeEnum.SFC.getValue()) {
			// 如果是顺风车
			int creatorCarOwnerTypeInt = getCreateCarOwnerTypeInt(creatorCarOwnerTypeField);
			if (creatorCarOwnerTypeInt == CarOwnerTypeEnum.CAR_OWNER.getValue()) {
				// 车主需要验证座位数
				if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
					return false;
				}
				return true;
			} else if (creatorCarOwnerTypeInt == CarOwnerTypeEnum.PASSENGER.getValue()) {
				// 乘客不需要验证座位数
				return true;
			}
			
		} else if(orderTypeInt == OrderTypeEnum.WORK.getValue()) {
			// 如果是上下班拼车
			
			int creatorCarOwnerTypeInt = getCreateCarOwnerTypeInt(creatorCarOwnerTypeField);
			if (creatorCarOwnerTypeInt == CarOwnerTypeEnum.PASSENGER.getValue()) {
				// 乘客不需要验证座位数
				return true;
			}	
			if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
				return false;
			}
			return true;
		}
		// 其他情况
		return true;
	}

	private int getCreateCarOwnerTypeInt(Field creatorCarOwnerTypeField) {
		if (creatorCarOwnerTypeField == null) {
			throw new SplistException("field 'creatorCarOwnerType' not exist!");
		}
		String creatorCarOwnerTypeValue = StringUtil.toString(creatorCarOwnerTypeField.getValue());
		int creatorCarOwnerTypeInt = ConvertUtil.toInt(creatorCarOwnerTypeValue, 0);
		return creatorCarOwnerTypeInt;
	}

}
