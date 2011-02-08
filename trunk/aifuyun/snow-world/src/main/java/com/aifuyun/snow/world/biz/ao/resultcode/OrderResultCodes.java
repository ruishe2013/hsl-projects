package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;

public interface OrderResultCodes {
	
	public static final ResultCode ORDER_NOT_EXIST = ResultCode.create("该拼单不存在。");
	
	public static final ResultCode CANNOT_EDIT_OTHERS_ORDER = ResultCode.create("不能编辑别人发起的拼单。");

}
