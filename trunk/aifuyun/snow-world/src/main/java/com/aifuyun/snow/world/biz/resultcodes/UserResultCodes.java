package com.aifuyun.snow.world.biz.resultcodes;

import com.zjuh.sweet.result.ResultCode;

public class UserResultCodes extends ResultCode {

	private static final long serialVersionUID = -486556453444106511L;

	public static final ResultCode USER_NOT_EXIST = ResultCode.create();
	
	public static final ResultCode USER_NOT_LOGIN = ResultCode.create();
	
	public static final ResultCode PASSWORD_INCORRET = ResultCode.create();
	
	public static final ResultCode CAN_NOT_MODIFY_OTHERS_INFO  = ResultCode.create();
	
	public static final ResultCode USERNAME_HAS_EXIST = ResultCode.create();
	
	public static final ResultCode SENSITIVITY_USER = ResultCode.create();
	
}
