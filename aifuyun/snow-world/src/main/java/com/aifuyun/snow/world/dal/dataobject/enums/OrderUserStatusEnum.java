package com.aifuyun.snow.world.dal.dataobject.enums;

public enum OrderUserStatusEnum {
	
	WAIT_CONFIRM(0, "待确认"),
	HAS_CANCEL(10, "已取消"),
	CONFIRMED(20, "已确认"),
	BEAN_OUT(30, "已踢出")
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
