package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;

public interface OrderResultCodes {
	
	public static final ResultCode ORDER_NOT_EXIST = ResultCode.create("��ƴ�������ڡ�");
	
	public static final ResultCode CANNOT_EDIT_OTHERS_ORDER = ResultCode.create("���ܱ༭���˷����ƴ����");

}
