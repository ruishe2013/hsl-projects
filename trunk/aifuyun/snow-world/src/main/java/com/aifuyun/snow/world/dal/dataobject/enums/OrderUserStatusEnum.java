package com.aifuyun.snow.world.dal.dataobject.enums;

public enum OrderUserStatusEnum {
	
	WAIT_CONFIRM(0, "��ȷ��"),
	HAS_CANCEL(10, "��ȡ��"),
	CONFIRMED(20, "��ȷ��"),
	BEAN_OUT(30, "���߳�")
	;
	
	private OrderUserStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static OrderUserStatusEnum valueOf(int value) {
		for (OrderUserStatusEnum s : OrderUserStatusEnum.values()) {
			if (s.getValue() == value) {
				return s;
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
