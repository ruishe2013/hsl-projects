package com.aifuyun.snow.world.biz.resultcodes;

import com.zjuh.sweet.result.ResultCode;

public class CommonResultCodes extends ResultCode {
	
	private static final long serialVersionUID = 2416244406785690210L;

	public static final ResultCode SYSTEM_ERROR = (ResultCode)ResultCode.create();

	public static final ResultCode USER_NOT_LOGIN = (ResultCode)ResultCode.create();

}
