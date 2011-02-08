package com.aifuyun.snow.world.dal.dataobject.enums;

public enum OrderUserRoleEnum {
	
	JOINER(0, "������"),
	CREATOR(1, "������")
	;
	
	private OrderUserRoleEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static OrderUserRoleEnum valueOf(int value) {
		for (OrderUserRoleEnum s : OrderUserRoleEnum.values()) {
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
