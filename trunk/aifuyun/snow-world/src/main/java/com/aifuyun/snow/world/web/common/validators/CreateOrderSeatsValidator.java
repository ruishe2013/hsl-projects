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
		
		// ƴ������
		Field orderTypeField = fields.get("type");
		if (orderTypeField == null) {
			throw new SplistException("field 'type' not exist!");
		}
		String orderType = StringUtil.toString(orderTypeField.getValue());
		
		int orderTypeInt = ConvertUtil.toInt(orderType, 0);
		
		if (orderTypeInt == OrderTypeEnum.TAXI.getValue()) {
			// ����ǳ��⳵������Ϊ��
			if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
				return false;
			}
			return true;
		} else if (orderTypeInt == OrderTypeEnum.SFC.getValue()) {
			// �����˳�糵
			Field creatorCarOwnerTypeField = fields.get("creatorCarOwnerType");
			if (creatorCarOwnerTypeField == null) {
				throw new SplistException("field 'creatorCarOwnerType' not exist!");
			}
			String creatorCarOwnerTypeValue = StringUtil.toString(creatorCarOwnerTypeField.getValue());
			int creatorCarOwnerTypeInt = ConvertUtil.toInt(creatorCarOwnerTypeValue, 0);
			if (creatorCarOwnerTypeInt == CarOwnerTypeEnum.CAR_OWNER.getValue()) {
				// ������Ҫ��֤��λ��
				if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
					return false;
				}
				return true;
			} else if (creatorCarOwnerTypeInt == CarOwnerTypeEnum.PASSENGER.getValue()) {
				// �˿Ͳ���Ҫ��֤��λ��
				return true;
			}
			
		} else if(orderTypeInt == OrderTypeEnum.WORK.getValue()) {
			// ��������°�ƴ��������Ϊ��
			if (StringUtil.isEmpty(StringUtil.trimToEmpty(value))) {
				return false;
			}
			return true;
		}
		// �������
		return true;
	}

}
