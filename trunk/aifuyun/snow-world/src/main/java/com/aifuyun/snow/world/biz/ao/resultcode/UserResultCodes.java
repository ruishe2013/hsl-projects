package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;

public class UserResultCodes {

	public static final ResultCode USERNAME_EXIST = ResultCode.create("该用户名已经存在。");
	
	public static final ResultCode SENSITIVITY_USER = ResultCode.create("对不起，该用户名不能被注册。");
	
}
