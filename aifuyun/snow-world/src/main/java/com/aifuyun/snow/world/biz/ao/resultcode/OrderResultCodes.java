package com.aifuyun.snow.world.biz.ao.resultcode;

import com.zjuh.sweet.result.ResultCode;

public interface OrderResultCodes {
	
	public static final ResultCode ORDER_NOT_EXIST = ResultCode.create("��ƴ�������ڡ�");
	
	public static final ResultCode CANNOT_EDIT_OTHERS_ORDER = ResultCode.create("���ܲ������˷����ƴ����");
	
	public static final ResultCode CANNOT_FIND_ORDER_CREATOR = ResultCode.create("δ���ҵ�ƴ����������Ϣ��");
	
	public static final ResultCode ORDER_HAS_BEEN_PROCESSED = ResultCode.create("��ƴ���Ѿ�����");
	
	public static final ResultCode ORDER_CREATOR_INFO_NOT_COMPLETE = ResultCode.create("���������ϲ�������");

}
