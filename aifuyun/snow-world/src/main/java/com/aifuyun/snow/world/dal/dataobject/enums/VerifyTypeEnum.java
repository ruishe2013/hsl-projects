package com.aifuyun.snow.world.dal.dataobject.enums;

import java.util.List;

import com.zjuh.sweet.lang.CollectionUtil;

/**
 * @author pister
 *
 */
public enum VerifyTypeEnum {
	
	OWNER_CORP_EMAIL(1, "���ڵ�λ�ʼ���֤"),
	IDCARD(2, "ʡ��֤��֤"),
	BANK(4, "������֤");
	
	private VerifyTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;
	
	public static List<VerifyTypeEnum> valuesFrom(int value) {
		List<VerifyTypeEnum> ret = CollectionUtil.newArrayList();
		for (VerifyTypeEnum e : values()) {
			if ((e.getValue() & value) == e.getValue()) {
				ret.add(e);
			}
		}
		return ret;
	}
	
	public static VerifyTypeEnum valueOf(int value) {
		for (VerifyTypeEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	
}
