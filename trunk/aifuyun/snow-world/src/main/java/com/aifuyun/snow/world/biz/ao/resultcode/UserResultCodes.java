package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;
import com.zjuh.sweet.result.ResultCodeTypeEnum;

public interface UserResultCodes {

	public static final ResultCode USER_NOT_EXIST = ResultCode.create("���û������ڡ�");
	
	public static final ResultCode CAN_NOT_MODIFY_OTHERS_INFO  = ResultCode.create("�����޸ı��˵���Ϣ��");
	
	public static final ResultCode USERNAME_HAS_EXIST = ResultCode.create("���û����Ѿ����ڡ�", ResultCodeTypeEnum.CURRENT_TARGET);
	
	public static final ResultCode SENSITIVITY_USER = ResultCode.create("�Բ��𣬸��û������ܱ�ע�ᡣ", ResultCodeTypeEnum.CURRENT_TARGET);
	
}
