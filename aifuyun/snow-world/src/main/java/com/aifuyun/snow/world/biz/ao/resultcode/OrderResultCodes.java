package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;

public interface OrderResultCodes {
	
	public static final ResultCode ORDER_NOT_EXIST = ResultCode.create("该拼单不存在。");
	
	public static final ResultCode CANNOT_EDIT_OTHERS_ORDER = ResultCode.create("不能操作别人发起的拼单。");
	
	public static final ResultCode CANNOT_FIND_ORDER_CREATOR = ResultCode.create("未能找到拼单发起人信息。");
	
	public static final ResultCode ORDER_HAS_BEEN_PROCESSED = ResultCode.create("该拼单已经受理。");
	
	public static final ResultCode ORDER_CREATOR_INFO_NOT_COMPLETE = ResultCode.create("发起人资料不完整。");

}
