package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;
import com.zjuh.sweet.result.ResultCodeTypeEnum;

public interface UserResultCodes {

	public static final ResultCode USERNAME_HAS_EXIST = ResultCode.create("���û����Ѿ����ڡ�", ResultCodeTypeEnum.CURRENT_TARGET);
	
	public static final ResultCode SENSITIVITY_USER = ResultCode.create("�Բ��𣬸��û������ܱ�ע�ᡣ", ResultCodeTypeEnum.CURRENT_TARGET);
	
}
