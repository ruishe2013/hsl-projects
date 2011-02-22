package com.aifuyun.snow.world.biz.resultcodes;

import com.zjuh.sweet.result.ResultCode;

public class OrderResultCodes extends ResultCode {
	
	private static final long serialVersionUID = -5014979451087441574L;

	public static final ResultCode ORDER_NOT_EXIST = ResultCode.create();
	
	public static final ResultCode CANNOT_EDIT_OTHERS_ORDER = ResultCode.create();
	
	public static final ResultCode CANNOT_FIND_ORDER_CREATOR = ResultCode.create();
	
	public static final ResultCode ORDER_HAS_BEEN_PROCESSED = ResultCode.create();
	
	public static final ResultCode ORDER_CREATOR_INFO_NOT_COMPLETE = ResultCode.create();
	
	public static final ResultCode ORDER_CREATED_BY_YOURSELF = ResultCode.create();
	
	public static final ResultCode ORDER_HAS_BEEN_JOINED = ResultCode.create();
	
	public static final ResultCode ORDER_SEAT_IS_FULL = ResultCode.create();
	
	public static final ResultCode ONLY_OPERATE_WAIT_CONFIRM = ResultCode.create();
	
	public static final ResultCode CANNOT_CONFIRM_SELF = ResultCode.create();
	
	public static final ResultCode USER_NOT_JOIN_YET = ResultCode.create();
	
	public static final ResultCode ONLY_OPERATE_CONFIRM_JOIN = ResultCode.create();

}
