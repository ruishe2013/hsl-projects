package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;
import com.zjuh.sweet.result.ResultCodeTypeEnum;

public interface UserResultCodes {

	public static final ResultCode USERNAME_HAS_EXIST = ResultCode.create("该用户名已经存在。", ResultCodeTypeEnum.CURRENT_TARGET);
	
	public static final ResultCode SENSITIVITY_USER = ResultCode.create("对不起，该用户名不能被注册。", ResultCodeTypeEnum.CURRENT_TARGET);
	
}
