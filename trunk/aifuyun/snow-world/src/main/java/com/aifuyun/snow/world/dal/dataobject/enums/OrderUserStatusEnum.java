package com.aifuyun.snow.world.dal.dataobject.enums;

public enum OrderUserStatusEnum {
	
	NOT_CONFIRM(0, "待确认"),
	HAS_CANCEL(1, "已取消"),
	CONFIRMED(2, "已确认"),
	BEAN_OUT(3, "已踢出")
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
