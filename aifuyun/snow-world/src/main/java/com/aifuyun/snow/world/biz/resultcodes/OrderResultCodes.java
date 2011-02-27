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
	
	public static final ResultCode ORDER_SEAT_IS_FULL = ResultCode.create();
	
	public static final ResultCode ONLY_OPERATE_WAIT_CONFIRM = ResultCode.create();
	
	public static final ResultCode CANNOT_CONFIRM_SELF = ResultCode.create();
	
	public static final ResultCode CANNOT_REMOVE_SELF = ResultCode.create();
	
	public static final ResultCode USER_NOT_JOIN_YET = ResultCode.create();
	
	public static final ResultCode ONLY_OPERATE_CONFIRM_JOIN = ResultCode.create();
	
	public static final ResultCode USER_IN_WAIT_CONFIRM = ResultCode.create();
	
	public static final ResultCode USER_HAS_BEEN_JOINED = ResultCode.create();
	
	public static final ResultCode YOU_ARE_NOT_JOIN_YET = ResultCode.create();
	
	public static final ResultCode YOU_EXIT_WRONG_ORDER = ResultCode.create();
	
	public static final ResultCode ORDER_HAS_BEEN_CONFIRMED = ResultCode.create();
	
	public static final ResultCode ORDER_HAS_NOT_WAIT_CONFIRM = ResultCode.create();
	
	public static final ResultCode CAN_NOT_EXIT_ORDER_FOR_CONFIRMED = ResultCode.create();
	
	public static final ResultCode CAN_NOT_CONFIRM_EMPTY_JOINERS_ORDER = ResultCode.create();
	
	public static final ResultCode CAN_NOT_REMOVE_USER_FOR_CONFIRMED = ResultCode.create();
	
	public static final ResultCode INVALID_ROLE_VALUE = ResultCode.create();
	
	
}
